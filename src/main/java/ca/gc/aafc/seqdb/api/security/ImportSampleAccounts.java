package ca.gc.aafc.seqdb.api.security;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
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

  private final EntityManager entityManager;
  
  private final PasswordEncoder passwordEncoder;
  
  @Transactional
  @Override
  public void onApplicationEvent(ContextRefreshedEvent arg0) {
    log.info("Importing sample accounts...");
    
    Account adminAccount = new Account();
    adminAccount.setAccountName("Admin");
    adminAccount.setAccountPw(passwordEncoder.encode("Admin"));
    adminAccount.setAccountType("Admin");
    adminAccount.setAccountStatus("Active");
    entityManager.persist(adminAccount);
    
    Group adminGroup = new Group();
    adminGroup.setGroupName("Admin");
    entityManager.persist(adminGroup);
    
    AccountsGroup adminPermissions = new AccountsGroup();
    adminPermissions.setAccount(adminAccount);
    adminPermissions.setGroup(adminGroup);
    adminPermissions.setRights("1111");
    adminPermissions.setAdmin(true);
    entityManager.persist(adminPermissions);
    
    Account userAccount = new Account();
    userAccount.setAccountName("User");
    userAccount.setAccountPw(passwordEncoder.encode("User"));
    userAccount.setAccountType("User");
    userAccount.setAccountStatus("Active");
    entityManager.persist(userAccount);
    
    Group userGroup = new Group();
    userGroup.setGroupName("User");
    entityManager.persist(userGroup);
    
    AccountsGroup userPermissions = new AccountsGroup();
    userPermissions.setAccount(userAccount);
    userPermissions.setGroup(userGroup);
    userPermissions.setRights("1111");
    userPermissions.setAdmin(true);
    entityManager.persist(userPermissions);
    
    log.info("Imported sample accounts.");
  }

}
