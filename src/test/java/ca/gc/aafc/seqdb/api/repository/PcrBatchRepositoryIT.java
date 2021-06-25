package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.entities.PcrBatch;
import ca.gc.aafc.seqdb.api.service.PcrBatchService;
import ca.gc.aafc.seqdb.api.testsupport.factories.PcrBatchFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import io.crnk.core.queryspec.QuerySpec;

public class PcrBatchRepositoryIT extends BaseRepositoryTest {

  private PcrBatch testPcrBatch;

  //@Inject
  //protected PcrBatchRepository pcrBatchRepository;

  // private void createTestIndex() {
  //   testPcrBatch = PcrBatchFactory.newPcrBatch().build();
  //   PcrBatchRepository
    
  // }

  // @BeforeEach
  // public void setup() {
  //   createTestIndex();
  // }
  
  // @Test
  // public void findPcrBatch_whenPcrBatchExists_PcrBatchReturned() {
  //   PcrBatchDto dto = pcrBatchRepository.findOne(
  //       testPcrBatch.getUuid(),
  //       new QuerySpec(PcrBatchDto.class)
  //   );
    
  //   assertNotNull(dto);
  //   assertEquals("test index", testPcrBatch.getName());
  // }
  
  // @Test
  // public void createPcrBatch_onSuccess_PcrBatchCreated() {
  //   PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
  //   PcrBatchDto created = pcrBatchRepository.create(newDto);
  //   assertNotNull(created.getUuid());
  // }
  
  // @Test
  // public void updatePcrBatch_onSuccess_PcrBatchUpdated() {
  //   PcrBatchDto dto = PcrBatchRepository.findOne(
  //       testPcrBatch.getUuid(),
  //       new QuerySpec(PcrBatchDto.class)
  //   );
    
  //   dto.setName("updated name");
  //   PcrBatchRepository.save(dto);
  //   assertEquals("updated name", testPcrBatch.getName());
  // }
  
  // @Test
  // public void deletePcrBatch_onSuccess_PcrBatchDeleted() {
  //   PcrBatchRepository.delete(testPcrBatch.getUuid());
  //   assertNull(entityManager.find(PcrBatch.class, testPcrBatch.getId()));
  // }

  
}
