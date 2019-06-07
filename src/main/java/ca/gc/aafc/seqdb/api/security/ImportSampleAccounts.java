package ca.gc.aafc.seqdb.api.security;

import javax.inject.Inject;
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

import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Imports sample local Account data when the application starts.
 * You can log into these Accounts without a connection to LDAP.
 * 
 * Username: Admin, Password: Admin
 * Username: User, Password: User
 */
@Named
@ConditionalOnProperty(value = "import-sample-accounts", havingValue = "true")
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ImportSampleAccounts implements ApplicationListener<ContextRefreshedEvent> {

  public static final String IMPORTED_ADMIN_ACCOUNT_NAME = "Admin";
  public static final String IMPORTED_USER_ACCOUNT_NAME = "User";
  
  private final EntityManager entityManager;  
  private final PasswordEncoder passwordEncoder;
    
  @Transactional
  @Override
  public void onApplicationEvent(ContextRefreshedEvent arg0) {
    log.info("Importing sample accounts...");
        
    Account adminAccount = new Account();
    adminAccount.setAccountName(IMPORTED_ADMIN_ACCOUNT_NAME);
    adminAccount.setAccountPw(passwordEncoder.encode(IMPORTED_ADMIN_ACCOUNT_NAME));
    adminAccount.setAccountType(Account.Type.ADMIN.toString());
    adminAccount.setAccountStatus(Account.Status.ACTIVE.toString());
    entityManager.persist(adminAccount);
    
    AccountsGroup adminPermissions = new AccountsGroup();
    adminPermissions.setAccount(adminAccount);
    adminPermissions.setGroup(retrieveGroup("Admin Group"));
    adminPermissions.setRights("1111");
    adminPermissions.setAdmin(true);
    entityManager.persist(adminPermissions);
    
    Account userAccount = new Account();
    userAccount.setAccountName(IMPORTED_USER_ACCOUNT_NAME);
    userAccount.setAccountPw(passwordEncoder.encode(IMPORTED_USER_ACCOUNT_NAME));
    userAccount.setAccountType(Account.Type.USER.toString());
    userAccount.setAccountStatus(Account.Status.ACTIVE.toString());
    entityManager.persist(userAccount);
    
    AccountsGroup userPermissions = new AccountsGroup();
    userPermissions.setAccount(userAccount);
    userPermissions.setGroup(retrieveGroup("User Group"));
    userPermissions.setRights("1111");
    userPermissions.setAdmin(true);
    entityManager.persist(userPermissions);
        
    log.info("Imported sample accounts.");
  }
  
  /**
   * Retrieve a group that is already persisted in the database (usually by Liquibase initial-data)
   * @param groupName case sensitive groupName
   * @return
   */
  private Group retrieveGroup(String groupName) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Group> criteria = criteriaBuilder.createQuery(Group.class);
    Root<Group> root = criteria.from(Group.class);
    
    criteria.where(criteriaBuilder.equal(root.get("groupName"), groupName));
    criteria.select(root);
    
    TypedQuery<Group> query = entityManager.createQuery(criteria);
    return query.getSingleResult();
  }

}
