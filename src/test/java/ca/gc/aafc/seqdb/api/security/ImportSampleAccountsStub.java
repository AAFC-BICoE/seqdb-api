package ca.gc.aafc.seqdb.api.security;

import ca.gc.aafc.seqdb.entities.Group;

/**
 * For the ImportSampleAccount integration test. This is used to create an altered
 * version of the ImportSampleAccounts to override Hibernate methods and to use the
 * in memory entity manager.
 *
 */
public class ImportSampleAccountsStub extends ImportSampleAccounts {
  
  /**
   * Skip the hibernate query and just assume the accounts do not exist in this
   * test situation.
   */
  @Override
  protected boolean accountExists(String accountName) {
    return false;
  }
  
  @Override
  protected Group retrieveGroup(String groupName) {
    return new Group("Test Group");
  }
}
