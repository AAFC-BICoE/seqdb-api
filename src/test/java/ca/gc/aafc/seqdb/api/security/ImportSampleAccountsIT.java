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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ca.gc.aafc.seqdb.MapBackedEntityManager;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.entities.Account;

import static org.junit.Assert.*;

/**
 * Contains a bunch of different testing environments for testing the Import Sample Account
 * functionality. This test takes advantage of the MapBackedEntityManager which will mock
 * the EnityManager to just persist data into memory. This allows us to create separate
 * testing environments to see how the Import Sample Account performs in each.
 *
 * Each test has it's own TestConfiguration which will override some beans that way we can
 * test specific situations.
 */
@RunWith(Enclosed.class)
public class ImportSampleAccountsIT {

  /**
   * Testing environment where import sample accounts is enabled, the accounts do not exist, so they
   * should be persisted.
   */
  @RunWith(SpringJUnit4ClassRunner.class)
  @SpringBootTest(classes = ImportSampleAccountsEnabledIT
        .EnableImportSampleAccountsTestConfig.class)
  public static class ImportSampleAccountsEnabledIT {

    /**
     * Mocked entity manager to use instead of the real entity manager. The reason were using this
     * is so the changes don't persist into the real database. We just want to test to make sure
     * it triggers the persist.
     */
    private static final EntityManager ENTITY_MANAGER = new MapBackedEntityManager();

    @Inject
    private PasswordEncoder passwordEncoder;

    @TestConfiguration
    @Import(SeqdbApiLauncher.class)
    public static class EnableImportSampleAccountsTestConfig {

      @Inject
      private PasswordEncoder passwordEncoder;

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
        return new ImportSampleAccountsStub(ENTITY_MANAGER, passwordEncoder, false);
      }

      /**
       * Instead of running it with the property we just apply this bean with mocked
       * methods to avoid the hibernate queries from being performed.
       */
      @Bean
      @Primary
      public EntityManager mockEntityManager() {
        return ENTITY_MANAGER;
      }
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
      List<Object> persistedAccounts = ((MapBackedEntityManager) ENTITY_MANAGER)
              .getPersistedEntities(Account.class);

      // Check if the user was inserted properly.
      Account user = getUserFromPersistedData(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME,
          persistedAccounts);
      assertNotNull(user);
      assertTrue(passwordEncoder.matches(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME,
          user.getAccountPw()));
      assertEquals(Account.Type.USER.toString(), user.getAccountType());
      assertEquals(Account.Status.ACTIVE.toString(), user.getAccountStatus());

      // Check if the admin was inserted properly.
      Account admin = getUserFromPersistedData(ImportSampleAccounts.IMPORTED_ADMIN_ACCOUNT_NAME,
          persistedAccounts);
      assertNotNull(admin);
      assertTrue(passwordEncoder.matches(ImportSampleAccounts.IMPORTED_ADMIN_ACCOUNT_NAME,
          admin.getAccountPw()));
      assertEquals(Account.Type.ADMIN.toString(), admin.getAccountType());
      assertEquals(Account.Status.ACTIVE.toString(), admin.getAccountStatus());

    }
  }

  /**
   * Testing environment where import sample accounts is disabled, so no account data should be
   * persisted in this case.
   */
  @RunWith(SpringJUnit4ClassRunner.class)
  @SpringBootTest(classes = ImportSampleAccountsDisabledIT
        .DisableImportSampleAccountsTestConfig.class)
  public static class ImportSampleAccountsDisabledIT {

    /**
     * Mocked entity manager to use instead of the real entity manager. The reason were using this
     * is so the changes don't persist into the real database. We just want to test to make sure
     * it triggers the persist.
     */
    private static final EntityManager ENTITY_MANAGER = new MapBackedEntityManager();

    @TestConfiguration
    @Import(SeqdbApiLauncher.class)
    public static class DisableImportSampleAccountsTestConfig {

      /**
       * Instead of running it with the property we just apply this bean with mocked
       * methods to avoid the hibernate queries from being performed.
       */
      @Bean
      @Primary
      public EntityManager mockEntityManager() {
        return ENTITY_MANAGER;
      }
    }

    /**
     * User data should not be stored in the database since the import sample accounts is not
     * activated.
     */
    @Test
    public void startApp_whenImportSampleAccountsNotSet_sampleAccountsNotAvailable() {
      // Retrieve a list of persisted accounts.
      List<Object> persistedAccounts = ((MapBackedEntityManager) ENTITY_MANAGER)
          .getPersistedEntities(Account.class);

      // No accounts should be persisted.
      assertNull(persistedAccounts);
    }
  }

  /**
   * Testing environment where the import sample accounts is enabled, but the accounts already exist. This
   * test will be ensuring that no data is persisted in this case.
   */
  @RunWith(SpringJUnit4ClassRunner.class)
  @SpringBootTest(classes = ImportSampleAccountsEnabledAccountsExistIT
        .EnableImportSampleAccountsAlreadyExistTestConfig.class)
  public static class ImportSampleAccountsEnabledAccountsExistIT {
    /**
     * Mocked entity manager to use instead of the real entity manager. The reason were using this
     * is so the changes don't persist into the real database. We just want to test to make sure
     * it triggers the persist.
     */
    private static final EntityManager ENTITY_MANAGER = new MapBackedEntityManager();

    @TestConfiguration
    @Import(SeqdbApiLauncher.class)
    public static class EnableImportSampleAccountsAlreadyExistTestConfig {

      @Inject
      private PasswordEncoder passwordEncoder;

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
        return new ImportSampleAccountsStub(ENTITY_MANAGER, passwordEncoder, true);
      }

      /**
       * Instead of running it with the property we just apply this bean with mocked
       * methods to avoid the hibernate queries from being performed.
       */
      @Bean
      @Primary
      public EntityManager mockEntityManager() {
        return ENTITY_MANAGER;
      }
    }

    /**
     * User data should not be stored in the database since the import sample accounts is not
     * activated.
     */
    @Test
    public void startApp_whenImportSampleAccountsSet_sampleAccountsAlreadyExist() {
      // Retrieve a list of persisted accounts.
      List<Object> persistedAccounts = ((MapBackedEntityManager) ENTITY_MANAGER)
          .getPersistedEntities(Account.class);

      // No accounts should be persisted.
      assertNull(persistedAccounts);
    }
  }

  /**
   * Look at the persisted data and retrieve a specific user.
   *
   * @param userName the name of the user to find.
   * @param listOfPersisted the persisted data to search against.
   * @return if an account a found, the account will be returned. Otherwise, null is returned.
   */
  private static Account getUserFromPersistedData(String userName, List<Object> listOfPersisted) {
    for (Object object : listOfPersisted) {
      Account account = (Account) object;
      if (account.getAccountName().equals(userName)) {
        return account;
      }
    }
    return null;
  }

}
