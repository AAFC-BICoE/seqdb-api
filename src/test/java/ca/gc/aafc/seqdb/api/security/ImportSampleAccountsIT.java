package ca.gc.aafc.seqdb.api.security;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;

/**
 * Scope of this test is to ensure inserting sample accounts works properly. Calling either the
 * insertUserAccount should create the account in the entity manager.
 */
public class ImportSampleAccountsIT extends BaseIntegrationTest {

  private ImportSampleAccounts importSampleAccounts;

  @Autowired
  private EntityManager entityManager;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  /**
   * Create the import sample account with entity manager and password encoder.
   */
  @Before
  public void setup() {
    importSampleAccounts = new ImportSampleAccounts(entityManager, passwordEncoder);
  }
  
  /**
   * Test the insert user account to make the account is created and can be found in the entity
   * manager. And also ensures all of the values are valid.
   */
  @Test
  public void importSampleAccounts_insertUserAccount_successfullyCreateUserAccount() {
    // Invoke the create user accounts
    importSampleAccounts.insertUserAccount();

    // Check if the account exists on the entity manager...
    Account userAccount = importSampleAccounts
        .retrieveAccount(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME);

    assertNotNull(userAccount);
    assertNotNull(userAccount.getAccountId());
    assertEquals(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME, userAccount.getAccountName());
    assertTrue(passwordEncoder.matches(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME,
        userAccount.getAccountPw()));
    assertEquals(Account.Type.USER.toString(), userAccount.getAccountType());
    assertEquals(Account.Status.ACTIVE.toString(), userAccount.getAccountStatus());

    // Retrieve the account group from the entity manager...
    AccountsGroup accountGroup = importSampleAccounts
        .retrieveAccountGroup(userAccount.getAccountId());

    assertNotNull(accountGroup);
    assertEquals(ImportSampleAccounts.IMPORTED_ACCOUNT_RIGHTS, accountGroup.getRights());
    assertTrue(accountGroup.getAdmin());
    assertEquals(importSampleAccounts.retrieveGroup(ImportSampleAccounts.USER_GROUP_NAME),
        accountGroup.getGroup());
  }
  
  /**
   * Test the insert admin account to make the account is created and can be found in the entity
   * manager. And also ensures all of the values are valid.
   */
  @Test
  public void importSampleAccounts_insertAdminAccount_successfullyCreateAdminAccount() {
    // Invoke the create user accounts
    importSampleAccounts.insertAdminAccount();

    // Check if the account exists on the entity manager...
    Account adminAccount = importSampleAccounts
        .retrieveAccount(ImportSampleAccounts.IMPORTED_ADMIN_ACCOUNT_NAME);

    assertNotNull(adminAccount);
    assertNotNull(adminAccount.getAccountId());
    assertEquals(ImportSampleAccounts.IMPORTED_ADMIN_ACCOUNT_NAME, adminAccount.getAccountName());
    assertTrue(passwordEncoder.matches(ImportSampleAccounts.IMPORTED_ADMIN_ACCOUNT_NAME,
        adminAccount.getAccountPw()));
    assertEquals(Account.Type.ADMIN.toString(), adminAccount.getAccountType());
    assertEquals(Account.Status.ACTIVE.toString(), adminAccount.getAccountStatus());

    // Retrieve the account group from the entity manager...
    AccountsGroup accountGroup = importSampleAccounts
        .retrieveAccountGroup(adminAccount.getAccountId());

    assertNotNull(accountGroup);
    assertEquals(ImportSampleAccounts.IMPORTED_ACCOUNT_RIGHTS, accountGroup.getRights());
    assertTrue(accountGroup.getAdmin());
    assertEquals(importSampleAccounts.retrieveGroup(ImportSampleAccounts.ADMIN_GROUP_NAME),
        accountGroup.getGroup());
  }
}
