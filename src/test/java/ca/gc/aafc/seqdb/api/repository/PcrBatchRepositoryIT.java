package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

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

import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class PcrBatchRepositoryIT extends BaseRepositoryTest {

  @Inject
  protected PcrBatchRepository pcrBatchRepository;

  @Test
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
  public void createPcrBatch_onSuccess_PcrBatchCreated() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());
  }
  
  @Test
  public void updatePcrBatch_onSuccess_PcrBatchUpdated() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());

    PcrBatchDto found = pcrBatchRepository.findOne(
        created.getUuid(),
        new QuerySpec(PcrBatchDto.class)
    );

    UUID uuid = UUID.randomUUID();

    List<UUID> experimenters = List.of(uuid);
    
    found.setExperimenters(experimenters);;
    PcrBatchDto updated = pcrBatchRepository.save(found);
    assertEquals(uuid, updated.getExperimenters().get(0));
  }
  
  @Test
  public void deletePcrBatch_onSuccess_PcrBatchDeleted() {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    
    PcrBatchDto created = pcrBatchRepository.create(newDto);
    assertNotNull(created.getUuid());

    pcrBatchRepository.delete(created.getUuid());
    assertThrows(ResourceNotFoundException.class, () -> pcrBatchRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrBatchDto.class)
    ));
  }

  
}
