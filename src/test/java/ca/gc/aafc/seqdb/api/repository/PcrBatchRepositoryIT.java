package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.testsupport.security.WithMockKeycloakUser;
import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.entities.PcrBatch;
import ca.gc.aafc.seqdb.api.service.PcrBatchService;
import ca.gc.aafc.seqdb.api.testsupport.factories.PcrBatchFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;

import org.springframework.boot.test.context.SpringBootTest;

import io.crnk.core.queryspec.QuerySpec;

@SpringBootTest(properties = "keycloak.enabled=true")
public class PcrBatchRepositoryIT extends BaseRepositoryTest {

  @Inject
  protected PcrBatchRepository pcrBatchRepository;

  @Test
  @WithMockKeycloakUser(username = "test user", groupRole = {"aafc: staff"})
  public void findPcrBatch_whenPcrBatchExists_PcrBatchReturned() {

    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);

    PcrBatchDto found = pcrBatchRepository.findOne(
        created.getUuid(),
        new QuerySpec(PcrBatchDto.class)
    );
    
    assertNotNull(found);
    assertEquals(created.getGroup(), found.getGroup());
  }
  
  @Test
  @WithMockKeycloakUser(username = "test user", groupRole = {"aafc: staff"})
  public void createPcrBatch_onSuccess_PcrBatchCreated() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());
  }
  
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
