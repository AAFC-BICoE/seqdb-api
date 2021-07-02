package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.MolecularSampleDto;
import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularSampleTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrReactionTestFixture;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class PcrReactionRepositoryIT extends BaseRepositoryTest {
  
  @Inject
  private PcrBatchRepository pcrBatchRepository;
  
  @Inject
  private PcrReactionRepository pcrReactionRepository;

  @Inject
  private MolecularSampleRepository molecularSampleRepository;

  private PcrBatchDto pcrBatchTest;
  private MolecularSampleDto molecularSampleTest;

  @BeforeEach
  public void setup() {
    pcrBatchTest = pcrBatchRepository.create(PcrBatchTestFixture.newPcrBatch());
    molecularSampleTest = molecularSampleRepository.create(MolecularSampleTestFixture.newMolecularSample());
  }

  @Test
  public void createPcrReaction_onSuccess_PcrReactionCreated() {
    PcrReactionDto newDto = PcrReactionTestFixture.newPcrReaction();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setSample(molecularSampleTest);

    PcrReactionDto created = pcrReactionRepository.create(newDto);

    assertNotNull(created.getUuid());
    assertEquals(pcrBatchTest.getUuid(), created.getPcrBatch().getUuid());
    assertEquals(molecularSampleTest.getUuid(), created.getSample().getUuid());
    
  }

  @Test
  public void findPcrReaction_whenPcrReactionExists_PcrReactionReturned() {

    PcrReactionDto newDto = PcrReactionTestFixture.newPcrReaction();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setSample(molecularSampleTest);

    PcrReactionDto created = pcrReactionRepository.create(newDto);

    PcrReactionDto found = pcrReactionRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrReactionDto.class)
    );

    assertNotNull(found);
    assertEquals(created.getUuid(), found.getUuid());
    assertEquals(pcrBatchTest.getUuid(), found.getPcrBatch().getUuid());
    assertEquals(molecularSampleTest.getUuid(), found.getSample().getUuid());

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
    
    found.setSample(molecularSampleTest);
    
    PcrReactionDto updated = pcrReactionRepository.save(found);
    assertEquals(molecularSampleTest.getUuid(), updated.getSample().getUuid());
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
