package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.MolecularSampleDto;
import ca.gc.aafc.seqdb.api.dto.sanger.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.dto.sanger.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularSampleTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class PcrBatchItemRepositoryIT extends BaseRepositoryTest {
  
  @Inject
  private PcrBatchRepository pcrBatchRepository;
  
  @Inject
  private PcrBatchItemRepository pcrBatchItemRepository;

  @Inject
  private MolecularSampleRepository molecularSampleRepository;

  @Test
  public void createPcrBatchItem_onSuccess_PcrBatchItemCreated() {
    PcrBatchDto pcrBatchTest = pcrBatchRepository.create(PcrBatchTestFixture.newPcrBatch());
    MolecularSampleDto molecularSampleTest = molecularSampleRepository.create(MolecularSampleTestFixture.newMolecularSample()); 

    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setSample(molecularSampleTest);

    PcrBatchItemDto created = pcrBatchItemRepository.create(newDto);

    assertNotNull(created.getUuid());
    assertEquals(molecularSampleTest.getUuid(), created.getSample().getUuid());
    assertEquals(pcrBatchTest.getUuid(), created.getPcrBatch().getUuid());
    assertEquals(PcrBatchItemTestFixture.GROUP, created.getGroup());
    assertEquals(PcrBatchItemTestFixture.CREATED_BY, created.getCreatedBy());
    assertEquals(PcrBatchItemTestFixture.WELL_COLUMN, created.getWellColumn());
    assertEquals(PcrBatchItemTestFixture.WELL_ROW, created.getWellRow());

  }

  @Test
  public void findPcrBatchItem_whenPcrReactionExists_PcrBatchItemReturned() {

    PcrBatchDto pcrBatchTest = pcrBatchRepository.create(PcrBatchTestFixture.newPcrBatch());
    MolecularSampleDto molecularSampleTest = molecularSampleRepository.create(MolecularSampleTestFixture.newMolecularSample()); 
    
    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setSample(molecularSampleTest);

    PcrBatchItemDto created = pcrBatchItemRepository.create(newDto);

    PcrBatchItemDto found = pcrBatchItemRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrBatchItemDto.class)
    );

    assertNotNull(created.getUuid());
    assertEquals(molecularSampleTest.getUuid(), found.getSample().getUuid());
    assertEquals(pcrBatchTest.getUuid(), found.getPcrBatch().getUuid());
    assertEquals(PcrBatchItemTestFixture.GROUP, found.getGroup());
    assertEquals(PcrBatchItemTestFixture.CREATED_BY, found.getCreatedBy());
    assertEquals(PcrBatchItemTestFixture.WELL_COLUMN, found.getWellColumn());
    assertEquals(PcrBatchItemTestFixture.WELL_ROW, found.getWellRow());

  }

  @Test
  public void updatePcrBatchItem_onSuccess_PcrBatchItemUpdated() {
    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    
    PcrBatchItemDto created = pcrBatchItemRepository.create(newDto);
    
    PcrBatchItemDto found = pcrBatchItemRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrBatchItemDto.class)
      );
    
    assertNull(found.getPcrBatch());

    PcrBatchDto pcrBatchTest = pcrBatchRepository.create(PcrBatchTestFixture.newPcrBatch());
    found.setPcrBatch(pcrBatchTest);
        
    PcrBatchItemDto updated = pcrBatchItemRepository.save(found);
    assertEquals(pcrBatchTest.getUuid(), updated.getPcrBatch().getUuid());
  }

  @Test
  public void deletePcrReaction_onSuccess_PcrReactionDeleted() {
    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    
    PcrBatchItemDto created = pcrBatchItemRepository.create(newDto);
    assertNotNull(created.getUuid());

    pcrBatchItemRepository.delete(created.getUuid());
    assertThrows(ResourceNotFoundException.class, () -> pcrBatchItemRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrBatchItemDto.class)
    ));
  }
  

}
