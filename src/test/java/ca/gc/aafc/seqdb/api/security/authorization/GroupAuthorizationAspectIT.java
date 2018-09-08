package ca.gc.aafc.seqdb.api.security.authorization;

import java.io.Serializable;
import java.util.Collections;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import ca.gc.aafc.seqdb.api.dto.GroupDto;
import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import io.crnk.core.exception.ForbiddenException;
import io.crnk.core.exception.UnauthorizedException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class GroupAuthorizationAspectIT extends BaseRepositoryTest {

  @Inject
  private ResourceRepositoryV2<PcrBatchDto, Serializable> pcrBatchRepository;
  
  @Inject
  private ResourceRepositoryV2<GroupDto, Serializable> groupRepository;
  
  @Test(expected = ForbiddenException.class)
  public void findOne_whenResourceExistsButUserDoesNotHaveReadAccess_throwForbiddenException() {
    // User account
    Account testAccount = new Account();
    testAccount.setAccountName("testAccount");
    testAccount.setAccountType("User");
    entityManager.persist(testAccount);
    SecurityContextHolder.getContext().setAuthentication(
        new TestingAuthenticationToken(new User("testAccount", "", Collections.emptyList()), "")
    );
    
    // Readable group
    Group testGroup = new Group();
    testGroup.setGroupName("testGroup1");
    entityManager.persist(testGroup);
    
    // Write-only permission
    AccountsGroup group1ReadPermission = new AccountsGroup();
    group1ReadPermission.setAccount(testAccount);
    group1ReadPermission.setGroup(testGroup);
    group1ReadPermission.setRights("0100");
    group1ReadPermission.setAdmin(false);
    entityManager.persist(group1ReadPermission);
    
    // Persist batch belonging to the group.
    PcrBatch batch = persistTestPcrBatchWith22Reactions("batch1");
    batch.setGroup(testGroup);
    
    QuerySpec querySpec = new QuerySpec(PcrBatchDto.class);
    this.pcrBatchRepository.findOne(batch.getPcrBatchId(), querySpec);
  }
  
  @Test
  public void createPcrBatch_whenUserIsAuthorized_executeCreateWithNoException() {
    this.testCreate("1001");
  }
  
  @Test(expected = ForbiddenException.class)
  public void createPcrBatch_whenUserIsNotAuthorized_throwForbiddenException() {
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
    batchDto.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
    
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
    batchDto.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
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
  
  @Test(expected = ForbiddenException.class)
  public void savePcrBatch_whenUserIsNotAuthorized_throwForbiddenException() {
    this.testSave("1000");
  }
  
  @Test
  public void savePcrBatch_whenThePcrBatchHasNoGroup_executeSaveWithNoException() {
    AccountsGroup permission = setupAccountAndGroupAndAccountsGroup("0000");
    
    // Test batch belonging to no group
    PcrBatch batch = persistTestPcrBatchWith22Reactions("testBatch");
    
    PcrBatchDto batchDto = pcrBatchRepository.findOne(
        batch.getPcrBatchId(),
        new QuerySpec(PcrBatchDto.class)
    );
    
    batchDto.setName("editedName");
    
    // Remove the user's permission on this group
    entityManager.remove(permission);
    
    this.pcrBatchRepository.save(batchDto);
  }
  
  @Test
  public void deletePcrBatch_whenUserIsAuthorized_executeDeleteWithNoException() {
    this.testDelete("1010");
  }
  
  @Test(expected = ForbiddenException.class)
  public void deletePcrBatch_whenUserIsNotAuthorized_throwForbiddenException() {
    this.testDelete("1000");
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
    batchDto.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
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
