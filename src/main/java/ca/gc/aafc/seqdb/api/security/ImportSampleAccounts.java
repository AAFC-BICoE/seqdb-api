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
    insertUserAccounts();
  }
  
  public void insertUserAccounts() {
    insertUserAccount(IMPORTED_ADMIN_ACCOUNT_NAME, ADMIN_GROUP_NAME, Account.Type.ADMIN);
    insertUserAccount(IMPORTED_USER_ACCOUNT_NAME, USER_GROUP_NAME, Account.Type.USER);
  }

  /**
   * Check if the provided user accounts exist, if it does not exist then insert the accounts
   * into the entity manager to be used for developer and testing purposes.
   */
  private void insertUserAccount(String accountName, String accountGroup, Account.Type type) {
    log.info("Importing sample accounts...");
    
    if (!accountExists(accountName)) {
      Account account = new Account();
      account.setAccountName(accountName);
      account.setAccountPw(passwordEncoder.encode(accountName));
      account.setAccountType(type.toString());
      account.setAccountStatus(Account.Status.ACTIVE.toString());
      entityManager.persist(account);

      AccountsGroup permissions = new AccountsGroup();
      permissions.setAccount(account);
      permissions.setGroup(retrieveGroup(accountGroup));
      permissions.setRights(IMPORTED_ACCOUNT_RIGHTS);
      permissions.setAdmin(true);
      entityManager.persist(permissions);
      log.info("{} sample account imported.", accountName);
    } else {
      log.info("{} account already exist. Skipping.", accountName);
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
   * Check if an account is already in the database (case sensitive).
   *
   * @param accountName
   * @return
   */
  private boolean accountExists(String accountName) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<Account> root = criteria.from(Account.class);

    criteria.select(criteriaBuilder.count(root));
    criteria.where(criteriaBuilder.equal(root.get("accountName"), accountName));

    Long count = entityManager.createQuery(criteria).getSingleResult();
    if (count == null) {
      return false;
    }
    // accountName is UNIQUE
    return count.longValue() == 1L;
  }

}
