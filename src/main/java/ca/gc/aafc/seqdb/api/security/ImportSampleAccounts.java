package ca.gc.aafc.seqdb.api.security;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.annotations.VisibleForTesting;

import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;

import lombok.extern.slf4j.Slf4j;

/**
 * Imports sample local Account data when the application starts. You can log into these Accounts
 * without a connection to LDAP.
 *
 * Username: Admin, Password: Admin Username: User, Password: User
 */
@Named
@ConditionalOnProperty(value = "import-sample-accounts", havingValue = "true")
@Slf4j
public class ImportSampleAccounts implements ApplicationListener<ContextRefreshedEvent> {

  public static final String IMPORTED_ADMIN_ACCOUNT_NAME = "Admin";
  public static final String IMPORTED_USER_ACCOUNT_NAME = "User";
  public static final String IMPORTED_ACCOUNT_RIGHTS = "1111";
  
  // defined by Liquibase
  public static final String ADMIN_GROUP_NAME = "Admin Group";
  public static final String USER_GROUP_NAME = "User Group";

  private final EntityManager entityManager;
  private final PasswordEncoder passwordEncoder;
 
  public ImportSampleAccounts(EntityManager entityManager, PasswordEncoder passwordEncoder) {
    this.entityManager = entityManager;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  @Override
  public void onApplicationEvent(ContextRefreshedEvent arg0) {
    log.info("Importing sample accounts...");
    insertAdminAccount();
    insertUserAccount();
  }
  
  /**
   * Checks if the admin account exists and creates a sample admin account used for
   * development and testing.
   */
  public void insertAdminAccount() {
    if (retrieveAccount(IMPORTED_ADMIN_ACCOUNT_NAME) == null) {
      Account adminAccount = new Account();
      adminAccount.setAccountName(IMPORTED_ADMIN_ACCOUNT_NAME);
      adminAccount.setAccountPw(passwordEncoder.encode(IMPORTED_ADMIN_ACCOUNT_NAME));
      adminAccount.setAccountType(Account.Type.ADMIN.toString());
      adminAccount.setAccountStatus(Account.Status.ACTIVE.toString());
      entityManager.persist(adminAccount);

      AccountsGroup adminPermissions = new AccountsGroup();
      adminPermissions.setAccount(adminAccount);
      adminPermissions.setGroup(retrieveGroup(ADMIN_GROUP_NAME));
      adminPermissions.setRights(IMPORTED_ACCOUNT_RIGHTS);
      adminPermissions.setAdmin(true);
      entityManager.persist(adminPermissions);
      log.info("Admin sample account imported.");
    } else {
      log.info("Admin account already exist. Skipping.");
    }
  }
  
  /**
   * Checks if the user account exists and creates a sample user account used for
   * development and testing.
   */
  public void insertUserAccount() {
    if (retrieveAccount(IMPORTED_USER_ACCOUNT_NAME) == null) {
      Account userAccount = new Account();
      userAccount.setAccountName(IMPORTED_USER_ACCOUNT_NAME);
      userAccount.setAccountPw(passwordEncoder.encode(IMPORTED_USER_ACCOUNT_NAME));
      userAccount.setAccountType(Account.Type.USER.toString());
      userAccount.setAccountStatus(Account.Status.ACTIVE.toString());
      entityManager.persist(userAccount);

      AccountsGroup userPermissions = new AccountsGroup();
      userPermissions.setAccount(userAccount);
      userPermissions.setGroup(retrieveGroup(USER_GROUP_NAME));
      userPermissions.setRights(IMPORTED_ACCOUNT_RIGHTS);
      userPermissions.setAdmin(true);
      entityManager.persist(userPermissions);
      log.info("User sample account imported.");
    } else {
      log.info("User account already exist. Skipping.");
    }
  }

  /**
   * Retrieve a group that is already persisted in the database (usually by Liquibase initial-data)
   *
   * @param groupName
   *          case sensitive groupName
   * @return
   */
  @VisibleForTesting
  protected Group retrieveGroup(String groupName) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Group> criteria = criteriaBuilder.createQuery(Group.class);
    Root<Group> root = criteria.from(Group.class);

    criteria.where(criteriaBuilder.equal(root.get("groupName"), groupName));
    criteria.select(root);

    TypedQuery<Group> query = entityManager.createQuery(criteria);
    return query.getSingleResult();
  }

  /**
   * Retrieve an account that is in the database.
   *
   * @param accountName string of the account name to retrieve. (Case sensitive)
   * @return the account from the entityManager. Null if it does not exist.
   */
  @VisibleForTesting
  protected Account retrieveAccount(String accountName) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Account> criteria = criteriaBuilder.createQuery(Account.class);
    Root<Account> root = criteria.from(Account.class);

    criteria.where(criteriaBuilder.equal(root.get("accountName"), accountName));

    return entityManager.createQuery(criteria).getResultList().stream().findFirst().orElse(null);
  }
  
  /**
   * Retrieve the account group based on the account id.
   * 
   * @param accountId the id of the account to find the account group on.
   * @return the accountsGroup from the entityManager. Null if it does not exist.
   */
  protected AccountsGroup retrieveAccountGroup(int accountId) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<AccountsGroup> criteria = criteriaBuilder.createQuery(AccountsGroup.class);
    Root<AccountsGroup> root = criteria.from(AccountsGroup.class);

    criteria.where(criteriaBuilder.equal(root.join("account").get("accountId"), accountId));

    return entityManager.createQuery(criteria).getResultList().stream().findFirst().orElse(null);
  }
}
