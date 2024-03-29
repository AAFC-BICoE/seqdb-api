package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.inject.Inject;
import javax.validation.ValidationException;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.seqdb.api.entities.StorageRestriction;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import ca.gc.aafc.seqdb.api.testsupport.factories.StorageRestrictionFactory;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

import java.util.List;
import java.util.UUID;

public class PcrBatchItemRepositoryIT extends BaseRepositoryTest {

  private static final UUID TEST_MAT_SAMPLE_UUID = UUID.randomUUID();
  
  @Inject
  private PcrBatchRepository pcrBatchRepository;
  
  @Inject
  private PcrBatchItemRepository pcrBatchItemRepository;

  @Test
  public void createPcrBatchItem_onSuccess_PcrBatchItemCreated() {
    PcrBatchDto pcrBatchTest = pcrBatchRepository.create(PcrBatchTestFixture.newPcrBatch());

    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setMaterialSample(ExternalRelationDto.builder().id(TEST_MAT_SAMPLE_UUID.toString()).type("material-sample").build());

    PcrBatchItemDto created = pcrBatchItemRepository.create(newDto);

    assertNotNull(created.getUuid());
    assertEquals(TEST_MAT_SAMPLE_UUID.toString(), created.getMaterialSample().getId());
    assertEquals(pcrBatchTest.getUuid(), created.getPcrBatch().getUuid());
    assertEquals(PcrBatchItemTestFixture.GROUP, created.getGroup());
    assertEquals(PcrBatchItemTestFixture.CREATED_BY, created.getCreatedBy());
    assertEquals(PcrBatchItemTestFixture.WELL_COLUMN, created.getWellColumn());
    assertEquals(PcrBatchItemTestFixture.WELL_ROW, created.getWellRow());
    assertEquals(PcrBatchItemTestFixture.RESULT, created.getResult());
  }

  @Test
  public void findPcrBatchItem_whenPcrReactionExists_PcrBatchItemReturned() {

    PcrBatchDto pcrBatchTest = pcrBatchRepository.create(PcrBatchTestFixture.newPcrBatch());
    
    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setMaterialSample(ExternalRelationDto.builder().id(TEST_MAT_SAMPLE_UUID.toString()).type("material-sample").build());

    PcrBatchItemDto created = pcrBatchItemRepository.create(newDto);

    PcrBatchItemDto found = pcrBatchItemRepository.findOne(
      created.getUuid(),
      new QuerySpec(PcrBatchItemDto.class)
    );

    assertNotNull(created.getUuid());
    assertEquals(TEST_MAT_SAMPLE_UUID.toString(), found.getMaterialSample().getId());
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

  @Test
  public void pcrBatchItem_onInvalidCoordinates_ExceptionThrown() {
    StorageRestriction storageRestriction = StorageRestrictionFactory.newStorageRestriction().build();
    PcrBatchDto batchDto = PcrBatchTestFixture.newPcrBatch();
    batchDto.setStorageRestriction(storageRestriction);
    PcrBatchDto pcrBatchTest = pcrBatchRepository.create(batchDto);

    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setWellRow("A");
    newDto.setWellColumn(1);
    newDto.setMaterialSample(ExternalRelationDto.builder().id(TEST_MAT_SAMPLE_UUID.toString()).type("material-sample").build());
    pcrBatchItemRepository.create(newDto);

    // try another one with invalid coordinates
    PcrBatchItemDto newDto2 = PcrBatchItemTestFixture.newPcrBatchItem();
    newDto2.setPcrBatch(pcrBatchTest);
    newDto2.setWellRow("A");
    newDto2.setWellColumn(StorageRestrictionFactory.DEFAULT_NUM_OF_COLUMNS + 1);
    newDto2.setMaterialSample(ExternalRelationDto.builder().id(TEST_MAT_SAMPLE_UUID.toString()).type("material-sample").build());
    assertThrows(ValidationException.class, ()-> pcrBatchItemRepository.create(newDto2));
  }

  @Test
  public void pcrBatchItem_onRowColumn_cellNumberCalculated() {
    StorageRestriction storageRestriction = StorageRestrictionFactory.newStorageRestriction().build();
    PcrBatchDto batchDto = PcrBatchTestFixture.newPcrBatch();
    batchDto.setStorageRestriction(storageRestriction);
    PcrBatchDto pcrBatchTest = pcrBatchRepository.create(batchDto);

    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    newDto.setPcrBatch(pcrBatchTest);
    newDto.setWellRow("A");
    newDto.setWellColumn(2);
    newDto.setMaterialSample(ExternalRelationDto.builder().id(TEST_MAT_SAMPLE_UUID.toString()).type("material-sample").build());
    newDto = pcrBatchItemRepository.create(newDto);

    PcrBatchItemDto found = pcrBatchItemRepository.findOne(
            newDto.getUuid(), new QuerySpec(PcrBatchItemDto.class));
    assertEquals(2, found.getCellNumber());
  }

}
