package ca.gc.aafc.seqdb.api.security;

import javax.persistence.EntityManager;

import org.springframework.security.crypto.password.PasswordEncoder;

import ca.gc.aafc.seqdb.entities.Group;

/**
 * For the ImportSampleAccount integration test. This is used to create an altered
 * version of the ImportSampleAccounts to override Hibernate methods and to use the
 * in memory entity manager.
 *
 */
public class ImportSampleAccountsStub extends ImportSampleAccounts {
  
  /**
   * The state of the accounts existing, this boolean will be returned as the override accountsExist method.
   * So depending on how the stub in configured you can change the return. 
   */
  private boolean accountsAlreadyExist = false;
 
  public ImportSampleAccountsStub(EntityManager entityManager, PasswordEncoder passwordEncoder, boolean accountsExist) {
    super(entityManager, passwordEncoder);
    this.accountsAlreadyExist = accountsExist;
  }

  /**
   * Skip the hibernate query and just assume the accounts do not exist in this
   * test situation.
   */
  @Override
  protected boolean accountExists(String accountName) {
    return accountsAlreadyExist;
  }
  
  @Override
  protected Group retrieveGroup(String groupName) {
    return new Group("Test Group");
  }
}
