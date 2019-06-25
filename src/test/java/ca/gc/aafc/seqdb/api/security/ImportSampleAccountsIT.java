package ca.gc.aafc.seqdb.api.security;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.gc.aafc.seqdb.MapBackedEntityManager;
import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;

@RunWith(Enclosed.class)
public class ImportSampleAccountsIT {

  @SpringBootTest(classes = ImportSampleAccountsEnabledIT.TestConfig.class)
  public static class ImportSampleAccountsEnabledIT extends BaseIntegrationTest {

    /**
     * Mocked entity manager to use instead of the real entity manager. The reason were using this
     * is so the changes don't persist into the real database. We just want to test to make sure
     * it triggers the persist.
     */
    private static MapBackedEntityManager mapBackedEntityManager = new MapBackedEntityManager();
    
    /**
     * This is a custom configuration to load in our stubbed ImportSampleAccount.
     * All of the other defaults are taken from the SeqdbApiLauncher class.
     */
    @TestConfiguration
    @Import(SeqdbApiLauncher.class)
    public static class TestConfig {

      /**
       * Instead of running it with the property we just apply this bean with mocked
       * methods to avoid the hibernate queries from being performed.
       * 
       * This is what also activates the bean to run which so it does not need the
       * conditional on property since the bean is being explicitly ran here. 
       */
      @Bean
      @Primary
      public ImportSampleAccounts mockedBean() {
        return new ImportSampleAccountsStub();
      }
      
      /**
       * Instead of running it with the property we just apply this bean with mocked
       * methods to avoid the hibernate queries from being performed.
       */
      @Bean
      @Primary
      public EntityManager mockEntityManager() {
        return mapBackedEntityManager;
      }
    }
    
    /**
     * Used to encode the password to ensure the password is encrypted correctly.
     */
    @Inject
    private PasswordEncoder passwordEncoder;
    
    /**
     * Look at the persisted data and retrieve a specific user.
     * 
     * @param userName the name of the user to find.
     * @param listOfPersisted the persisted data to search against.
     * @return if an account a found, the account will be returned. Otherwise, null is returned.
     */
    private Account getUserFromPersistedData(String userName, List<Object> listOfPersisted) {
      for (Object object : listOfPersisted) {
        Account account = (Account) object;
        
        if (account.getAccountName().equals(userName)) {
          return account;
        }
      }
      
      return null;
    }
    
    /**
     * Test to ensure that data is persisted whenever the import sample accounts is activated.
     * 
     * @see TestConfig - Configuration to activate the import sample account and mocking of the
     *    entity manager.
     */
    @Test
    public void startApp_whenImportSampleAccountsTrue_sampleAccountsAvailable() {
      
      // Retrieve a list of persisted accounts.
      List<Object> persistedAccounts = mapBackedEntityManager.getPersistedEntities(Account.class);
      
      // Check if the user was inserted properly.
      Account user = getUserFromPersistedData(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME, persistedAccounts);
      assertNotNull(user);
      assertTrue(passwordEncoder.matches(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME, user.getAccountPw()));
      assertEquals(Account.Type.USER.toString(), user.getAccountType());
      assertEquals(Account.Status.ACTIVE.toString(), user.getAccountStatus());
     
      // Check if the admin was inserted properly.
      Account admin = getUserFromPersistedData(ImportSampleAccounts.IMPORTED_ADMIN_ACCOUNT_NAME, persistedAccounts);
      assertNotNull(admin);
      assertTrue(passwordEncoder.matches(ImportSampleAccounts.IMPORTED_ADMIN_ACCOUNT_NAME, admin.getAccountPw()));
      assertEquals(Account.Type.ADMIN.toString(), admin.getAccountType());
      assertEquals(Account.Status.ACTIVE.toString(), admin.getAccountStatus());
      
    }
    
  }
  
  public static class ImportSampleAccountsDisabledIT extends BaseIntegrationTest {
    
    @Inject
    private AccountRepository accountRepository;
    
    /**
     * User data should not be stored in the database since the import sample accounts is not
     * activated.
     */
    @Test
    public void startApp_whenImportSampleAccountsNotSet_sampleAccountsNotAvailable() {
      assertNull(this.accountRepository.findByAccountNameIgnoreCase("Admin"));
      assertNull(this.accountRepository.findByAccountNameIgnoreCase("User"));
    }
    
  }
  
}
