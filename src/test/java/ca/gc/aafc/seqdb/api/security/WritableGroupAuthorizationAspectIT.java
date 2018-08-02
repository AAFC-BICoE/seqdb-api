package ca.gc.aafc.seqdb.api.security;

import java.util.Collections;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import ca.gc.aafc.seqdb.api.dto.GroupDto;
import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import io.crnk.core.exception.UnauthorizedException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class WritableGroupAuthorizationAspectIT extends BaseRepositoryTest {

  @Inject
  private AccountRepository accountRepository;
  
  private ResourceRepositoryV2<PcrBatchDto, Integer> pcrBatchRepository;
  private ResourceRepositoryV2<GroupDto, Integer> groupRepository;
  
  /**
   * Get the repository facades from crnk, which will invoke all filters, decorators, etc.
   */
  @Before
  public void initRepositories() {
    this.pcrBatchRepository = this.resourceRegistry.getEntry(PcrBatchDto.class)
        .getResourceRepositoryFacade();
    this.groupRepository = this.resourceRegistry.getEntry(GroupDto.class)
        .getResourceRepositoryFacade();
  }
  
  @Test
  public void createPcrBatch_whenUserIsAuthorized_executeCreateWithNoException() {
    this.testCreate("1001");
  }
  
  @Test(expected = UnauthorizedException.class)
  public void createPcrBatch_whenUserIsNotAuthorized_throwUnauthorizedException() {
    this.testCreate("1000");
  }
  
  @Test(expected = UnauthorizedException.class)
  public void createPcrBatch_whenUserIsNotLoggedIn_throwUnauthorizedException() {
    // Set authentication with a name that does not correspond to a persisted Account.
    SecurityContextHolder.getContext().setAuthentication(
        new TestingAuthenticationToken(new User("nonExistentAccount", "", Collections.emptyList()),
            "")
    );
    
    // Setup a batch dto without a group.
    PcrBatchDto batchDto = new PcrBatchDto();
    batchDto.setName("testBatch");
    batchDto.setType(PcrBatchType.SANGER);
    
    // Try to persist the batch dto without being authenticated as a persisted Account.
    pcrBatchRepository.create(batchDto);
  }
  
  @Test
  public void createPcrBatch_whenUserIsAdmin_executeCreateWithNoException() {
    AccountsGroup ag = setupAccountAndGroupAndAccountsGroup("0000");
    ag.getAccount().setAccountType("Admin");
    
    // Setup a batch dto for the new group.
    PcrBatchDto batchDto = new PcrBatchDto();
    batchDto.setName("testBatch");
    batchDto.setType(PcrBatchType.SANGER);
    batchDto.setGroup(
        groupRepository.findOne(ag.getGroup().getGroupId(), new QuerySpec(GroupDto.class))
    );
    
    // Try to persist the batch as an admin without explicit permissions on the group.
    pcrBatchRepository.create(batchDto);
  }
  
  @Test
  public void savePcrBatch_whenUserIsAuthorized_executeSaveWithNoException() {
    this.testSave("1100");
  }
  
  @Test(expected = UnauthorizedException.class)
  public void savePcrBatch_whenUserIsNotAuthorized_throwUnauthorizedException() {
    this.testSave("1000");
  }
  
  @Test
  public void deletePcrBatch_whenUserIsAuthorized_executeDeleteWithNoException() {
    this.testDelete("1010");
  }
  
  @Test(expected = UnauthorizedException.class)
  public void deletePcrBatch_whenUserIsNotAuthorized_throwUnauthorizedException() {
    this.testDelete("0000");
  }
  
  /**
   * Sets up an Account, Group, and AccountsGroup record.
   * @param rights The current user's rghts on the test Group that is created.
   * @return 
   */
  private AccountsGroup setupAccountAndGroupAndAccountsGroup(String rights) {
    // Test account
    Account testAccount = new Account();
    testAccount.setAccountName("testAccount");
    testAccount.setAccountType("User");
    entityManager.persist(testAccount);
    SecurityContextHolder.getContext().setAuthentication(
        new TestingAuthenticationToken(new User("testAccount", "", Collections.emptyList()), "")
    );
    
    // Test group
    Group testGroup = new Group();
    testGroup.setGroupName("testGroup");
    entityManager.persist(testGroup);
    
    // Delete permission for testAccount on testGroup
    AccountsGroup testPermission = new AccountsGroup();
    testPermission.setAccount(testAccount);
    testPermission.setGroup(testGroup);
    testPermission.setRights(rights);
    testPermission.setAdmin(false);
    entityManager.persist(testPermission);
    
    return testPermission;
  }
  
  /**
   * Tests a create operation.
   * @param rights The current user's rights for the test Group that is created
   */
  private void testCreate(String rights) {
    AccountsGroup permission = setupAccountAndGroupAndAccountsGroup(rights);
    
    PcrBatchDto batchDto = new PcrBatchDto();
    batchDto.setName("testBatch");
    batchDto.setType(PcrBatchType.SANGER);
    batchDto.setGroup(
        this.groupRepository.findOne(
            permission.getGroup().getGroupId(),
            new QuerySpec(GroupDto.class)
        )
    );
    
    pcrBatchRepository.create(batchDto);
  }
  
  /**
   * Tests a save operation.
   * @param rights The current user's rights for the test Group that is created
   */
  private void testSave(String rights) {
    AccountsGroup permission = setupAccountAndGroupAndAccountsGroup(rights);
    
    // Test batch belonging to the testGroup
    PcrBatch batch = persistTestPcrBatchWith22Reactions("testBatch");
    batch.setGroup(permission.getGroup());
    
    PcrBatchDto batchDto = pcrBatchRepository.findOne(
        batch.getPcrBatchId(),
        new QuerySpec(PcrBatchDto.class)
    );
    
    batchDto.setName("editedName");
    
    this.pcrBatchRepository.save(batchDto);
  }
  
  /**
   * Tests a delete operation.
   * @param rights The current user's rights for the test Group that is created
   */
  private void testDelete(String rights) {
    AccountsGroup permission = setupAccountAndGroupAndAccountsGroup(rights);
    
    // Test batch belonging to the testGroup
    PcrBatch batch = persistTestPcrBatchWith22Reactions("testBatch");
    batch.setGroup(permission.getGroup());
    
    pcrBatchRepository.delete(batch.getId());
  }
  
}
