package ca.gc.aafc.seqdb.api.security;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;

@RunWith(Enclosed.class)
public class ImportSampleAccountsIT {

  @TestPropertySource(properties="import-sample-accounts=true")
  public static class ImportSampleAccountsEnabledIT extends BaseIntegrationTest {
    
    @Inject
    private AccountRepository accountRepository;
    
    @Inject
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void startApp_whenImportSampleAccountsTrue_sampleAccountsAvailable() {
      // Check Admin account data
      Account adminAccount = accountRepository.findByAccountNameIgnoreCase("admin");
      assertTrue(passwordEncoder.matches("Admin", adminAccount.getAccountPw()));
      assertEquals("Admin", adminAccount.getAccountType());
      assertEquals("Active", adminAccount.getAccountStatus());
      
      AccountsGroup adminPermission = adminAccount.getAccountsGroups().iterator().next();
      assertEquals("1111", adminPermission.getRights());
      assertEquals(Boolean.TRUE, adminPermission.getAdmin());
      
      Group adminGroup = adminPermission.getGroup();
      assertEquals("Admin", adminGroup.getGroupName());
      
      // Check User account data
      Account userAccount = accountRepository.findByAccountNameIgnoreCase("user");
      assertTrue(passwordEncoder.matches("User", userAccount.getAccountPw()));
      assertEquals("User", userAccount.getAccountType());
      assertEquals("Active", userAccount.getAccountStatus());
      
      AccountsGroup userPermission = userAccount.getAccountsGroups().iterator().next();
      assertEquals("1111", userPermission.getRights());
      assertEquals(Boolean.TRUE, userPermission.getAdmin());
      
      Group userGroup = userPermission.getGroup();
      assertEquals("User", userGroup.getGroupName());
    }
    
  }
  
  public static class ImportSampleAccountsDisabledIT extends BaseIntegrationTest {
    
    @Inject
    private AccountRepository accountRepository;
    
    @Test
    public void startApp_whenImportSampleAccountsNotSet_sampleAccountsNotAvailable() {
      assertNull(this.accountRepository.findByAccountNameIgnoreCase("Admin"));
      assertNull(this.accountRepository.findByAccountNameIgnoreCase("User"));
    }
    
  }
  
}
