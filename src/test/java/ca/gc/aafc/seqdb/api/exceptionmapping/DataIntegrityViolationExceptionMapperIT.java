package ca.gc.aafc.seqdb.api.exceptionmapping;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import ca.gc.aafc.seqdb.api.dto.GroupDto;
import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import io.crnk.core.engine.document.ErrorData;
import io.crnk.core.engine.error.ErrorResponse;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class DataIntegrityViolationExceptionMapperIT extends BaseRepositoryTest {
  
  @Inject
  private DataIntegrityViolationExceptionMapper exceptionMapper;
  
  @Inject
  private ResourceRepositoryV2<PcrBatchDto, Serializable> pcrBatchRepository;
  
  @Inject
  private ResourceRepositoryV2<GroupDto, Serializable> groupRepository;
  
  @Test
  public void createPcrBatch_whenUniqueConstraintIsViolated_mapperCreatesReadableErrorMessages() {
    // Test group
    Group testGroup = new Group();
    testGroup.setGroupName("testGroup");
    this.entityManager.persist(testGroup);
    GroupDto testGroupDto = this.groupRepository.findOne(
        testGroup.getGroupId(),
        new QuerySpec(GroupDto.class)
    );
    
    // Create 2 batchs violating the unique constraint (name + group)
    PcrBatchDto batch1 = new PcrBatchDto();
    batch1.setName("batch");
    batch1.setType(PcrBatchType.SANGER);
    batch1.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
    batch1.setGroup(testGroupDto);
    
    PcrBatchDto batch2 = new PcrBatchDto();
    batch2.setName("batch");
    batch2.setType(PcrBatchType.SANGER);
    batch2.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
    batch2.setGroup(testGroupDto);
    
    this.pcrBatchRepository.create(batch1);
    try {
      // Attempt creating a second PcrBatch with the same Name and Group.
      this.pcrBatchRepository.create(batch2);
    } catch(DataIntegrityViolationException exception) {
      ErrorResponse errorResponse = this.exceptionMapper.toErrorResponse(exception);
      
      // Assert correct http status.
      assertEquals(422, errorResponse.getHttpStatus());
      
      List<ErrorData> errors = errorResponse.getErrors().stream().collect(Collectors.toList());
      
      // 1 error should be given.
      assertEquals(1, errors.size());
      
      // Assert correct error message, status and title (@Size name length error)
      assertEquals("could not execute statement; SQL", errors.get(0).getDetail().substring(0, 32));
      assertEquals("422", errors.get(0).getStatus());
      assertEquals("Data integrity violation", errors.get(0).getTitle());
      
      return;
    }
    
    // This test method should end at the "return" in the catch block.
    fail("DataIntegrityViolationException not thrown");
  }
  
}
