package ca.gc.aafc.seqdb.api.security.authorization;

import java.io.Serializable;
import java.util.Collections;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;

public class ReadableGroupFilterHandlerIT extends BaseRepositoryTest {
  
  @Inject
  private ResourceRepositoryV2<PcrBatchDto, Serializable> pcrBatchRepository;
  
  @Test
  public void findAll_whenNoFilterIsSpecified_filterByReadableGroups() {
    Account testAccount = new Account();
    testAccount.setAccountName("testAccount");
    testAccount.setAccountType("User");
    entityManager.persist(testAccount);
    SecurityContextHolder.getContext().setAuthentication(
        new TestingAuthenticationToken(new User("testAccount", "", Collections.emptyList()), "")
    );
    
    // Readable group
    Group testGroup1 = new Group();
    testGroup1.setGroupName("testGroup1");
    entityManager.persist(testGroup1);
    
    // Non-readable group
    Group testGroup2 = new Group();
    testGroup2.setGroupName("testGroup2");
    entityManager.persist(testGroup2);
    
    AccountsGroup group1ReadPermission = new AccountsGroup();
    group1ReadPermission.setAccount(testAccount);
    group1ReadPermission.setGroup(testGroup1);
    group1ReadPermission.setRights("1000");
    group1ReadPermission.setAdmin(false);
    entityManager.persist(group1ReadPermission);
    
    PcrBatch batch1 = persistTestPcrBatchWith22Reactions("batch1");
    PcrBatch batch2 = persistTestPcrBatchWith22Reactions("batch2");
    PcrBatch batch3 = persistTestPcrBatchWith22Reactions("batch3");
    
    // Should be readable because the user has read access on group1.
    batch1.setGroup(testGroup1);
    // Should be non-readable because the user does not have read access on group2.
    batch2.setGroup(testGroup2);
    // Should be readable because the batch does not belong to a group.
    batch3.setGroup(null);
    
    QuerySpec querySpec = new QuerySpec(PcrBatchDto.class);
    ResourceList<PcrBatchDto> batchDtos = this.pcrBatchRepository.findAll(querySpec);
    
    // Only the 2 readable batches should be found.
    assertEquals(2, batchDtos.size());
    assertEquals(batch1.getPcrBatchId(), batchDtos.get(0).getPcrBatchId());
    assertEquals(batch3.getPcrBatchId(), batchDtos.get(1).getPcrBatchId());
  }
  
  @Test
  public void findAll_whenUserisAdminAndNoFilterIsSpecified_returnAllResults() {
    // Admin account
    Account testAccount = new Account();
    testAccount.setAccountName("testAccount");
    testAccount.setAccountType("Admin");
    entityManager.persist(testAccount);
    SecurityContextHolder.getContext().setAuthentication(
        new TestingAuthenticationToken(new User("testAccount", "", Collections.emptyList()), "")
    );
    
    // Readable group
    Group testGroup1 = new Group();
    testGroup1.setGroupName("testGroup1");
    entityManager.persist(testGroup1);
    
    // Non-readable group
    Group testGroup2 = new Group();
    testGroup2.setGroupName("testGroup2");
    entityManager.persist(testGroup2);
    
    AccountsGroup group1ReadPermission = new AccountsGroup();
    group1ReadPermission.setAccount(testAccount);
    group1ReadPermission.setGroup(testGroup1);
    group1ReadPermission.setRights("1000");
    group1ReadPermission.setAdmin(false);
    entityManager.persist(group1ReadPermission);
    
    PcrBatch batch1 = persistTestPcrBatchWith22Reactions("batch1");
    PcrBatch batch2 = persistTestPcrBatchWith22Reactions("batch2");
    PcrBatch batch3 = persistTestPcrBatchWith22Reactions("batch3");
    
    // The user has read permission on group1.
    batch1.setGroup(testGroup1);
    // The user does not have read permission on group2.
    batch2.setGroup(testGroup2);
    // The batch does not belong to a group.
    batch3.setGroup(null);
    
    QuerySpec querySpec = new QuerySpec(PcrBatchDto.class);
    ResourceList<PcrBatchDto> batchDtos = this.pcrBatchRepository.findAll(querySpec);
    
    // All 3 batches should be found.
    assertEquals(3, batchDtos.size());
    assertEquals(batch1.getPcrBatchId(), batchDtos.get(0).getPcrBatchId());
    assertEquals(batch2.getPcrBatchId(), batchDtos.get(1).getPcrBatchId());
    assertEquals(batch3.getPcrBatchId(), batchDtos.get(2).getPcrBatchId());
  }

}
