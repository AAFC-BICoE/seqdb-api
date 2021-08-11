package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;
import ca.gc.aafc.seqdb.api.dto.sanger.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrReactionTestFixture;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class PcrReactionRepositoryIT extends BaseRepositoryTest {
  
  @Inject
  private PcrBatchRepository pcrBatchRepository;
  
  @Inject
  private PcrReactionRepository pcrReactionRepository;

  private PcrBatchDto pcrBatchTest;

  @BeforeEach
  public void setup() {
    pcrBatchTest = pcrBatchRepository.create(PcrBatchTestFixture.newPcrBatch());
  }

  @Test
  public void createPcrReaction_onSuccess_PcrReactionCreated() {
    PcrReactionDto newDto = PcrReactionTestFixture.newPcrReaction();
    newDto.setPcrBatch(pcrBatchTest);

    PcrReactionDto created = pcrReactionRepository.create(newDto);

    assertNotNull(created.getUuid());
    assertEquals(pcrBatchTest.getUuid(), created.getPcrBatch().getUuid());
    assertEquals(PcrReactionTestFixture.GROUP, created.getGroup());
    assertEquals(PcrReactionTestFixture.RESULT, created.getResult());
    assertEquals(PcrReactionTestFixture.TARGET, created.getTarget());
    
  }

  @Test
  public void findPcrReaction_whenPcrReactionExists_PcrReactionReturned() {

    PcrReactionDto newDto = PcrReactionTestFixture.newPcrReaction();
    newDto.setPcrBatch(pcrBatchTest);

    PcrReactionDto created = pcrReactionRepository.create(newDto);

    PcrReactionDto found = pcrReactionRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrReactionDto.class)
    );

    assertNotNull(found);
    assertEquals(created.getUuid(), found.getUuid());
    assertEquals(pcrBatchTest.getUuid(), found.getPcrBatch().getUuid());
    assertEquals(PcrReactionTestFixture.GROUP, found.getGroup());
    assertEquals(PcrReactionTestFixture.RESULT, found.getResult());
    assertEquals(PcrReactionTestFixture.TARGET, found.getTarget());

  }

  @Test
  public void updatePcrReaction_onSuccess_PcrReactionUpdated() {

    PcrReactionDto newDto = PcrReactionTestFixture.newPcrReaction();
    
    PcrReactionDto created = pcrReactionRepository.create(newDto);
    assertNotNull(created.getUuid());

    PcrReactionDto found = pcrReactionRepository.findOne(
        created.getUuid(),
        new QuerySpec(PcrReactionDto.class)
    );

    found.setPcrBatch(pcrBatchTest);
        
    PcrReactionDto updated = pcrReactionRepository.save(found);
    assertEquals(pcrBatchTest.getUuid(), updated.getPcrBatch().getUuid());
  }

  @Test
  public void deletePcrReaction_onSuccess_PcrReactionDeleted() {
    PcrReactionDto newDto = PcrReactionTestFixture.newPcrReaction();
    
    PcrReactionDto created = pcrReactionRepository.create(newDto);
    assertNotNull(created.getUuid());

    pcrReactionRepository.delete(created.getUuid());
    assertThrows(ResourceNotFoundException.class, () -> pcrReactionRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrReactionDto.class)
    ));
  }
}
