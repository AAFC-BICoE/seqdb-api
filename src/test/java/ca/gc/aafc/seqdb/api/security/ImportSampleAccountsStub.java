package ca.gc.aafc.seqdb.api.security;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

import org.springframework.security.crypto.password.PasswordEncoder;

import ca.gc.aafc.seqdb.entities.Group;

/**
 * For the ImportSampleAccount integration test. This is used to create an altered version of the
 * ImportSampleAccounts to override Hibernate-based methods and to use an alternative entity
 * manager.
 *
 */
public class ImportSampleAccountsStub extends ImportSampleAccounts {

  private static final Group ADMIN_GROUP = new Group(ImportSampleAccounts.ADMIN_GROUP_NAME);
  private static final Group USER_GROUP = new Group(ImportSampleAccounts.USER_GROUP_NAME);
  private static final List<Group> GROUPS = Arrays.asList(ADMIN_GROUP, USER_GROUP);

  /**
   * The state of the accounts existing, this boolean will be returned as the override accountsExist
   * method. So depending on how the stub in configured you can change the return.
   */
  private boolean accountsAlreadyExist = false;

  public ImportSampleAccountsStub(EntityManager entityManager, PasswordEncoder passwordEncoder,
      boolean accountsExist) {
    super(entityManager, passwordEncoder);
    this.accountsAlreadyExist = accountsExist;
  }

  /**
   * Skip the hibernate query and return accountsAlreadyExist value
   */
  @Override
  protected boolean accountExists(String accountName) {
    return accountsAlreadyExist;
  }

  @Override
  protected Group retrieveGroup(String groupName) {
    return GROUPS.stream().filter(g -> g.getGroupName().equals(groupName)).findFirst().orElse(null);
  }
}
