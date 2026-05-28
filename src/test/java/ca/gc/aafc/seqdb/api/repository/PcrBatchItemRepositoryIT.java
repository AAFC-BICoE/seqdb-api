package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.external.MaterialSampleExternalDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class PcrBatchItemRepositoryIT extends BaseRepositoryTestV2 {

  private static final UUID TEST_MAT_SAMPLE_UUID = UUID.randomUUID();
  
  @Inject
  private PcrBatchRepository pcrBatchRepository;
  
  @Inject
  private PcrBatchItemRepository pcrBatchItemRepository;

  @Test
  public void createPcrBatchItem_onSuccess_PcrBatchItemCreated()
      throws ResourceGoneException, ResourceNotFoundException {
    PcrBatchDto pcrBatchTest = PcrBatchTestFixture.newPcrBatch();
    UUID pcrBatchUuid = createWithRepository(pcrBatchTest, pcrBatchRepository::onCreate);

    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    JsonApiDocument pcrBatchItemToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, PcrBatchDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(newDto),
        Map.of(
          "pcrBatch", JsonApiDocument.ResourceIdentifier.builder().id(pcrBatchUuid)
            .type(PcrPrimerDto.TYPENAME).build(),
          "materialSample", JsonApiDocument.ResourceIdentifier.builder().id(TEST_MAT_SAMPLE_UUID)
            .type(MaterialSampleExternalDto.EXTERNAL_TYPENAME).build()
        ));
    UUID pcrBatchitemUuid = createWithRepository(pcrBatchItemToCreate, pcrBatchItemRepository::onCreate);

    PcrBatchItemDto reloadedDto = pcrBatchItemRepository.getOne(pcrBatchitemUuid, "include=" +
      String.join(",", pcrBatchItemToCreate.getRelationships().keySet())).getDto();

    assertNotNull(reloadedDto.getUuid());
    assertEquals(TEST_MAT_SAMPLE_UUID.toString(), reloadedDto.getMaterialSample().getId());
    assertEquals(pcrBatchUuid, reloadedDto.getPcrBatch().getUuid());
    assertEquals(PcrBatchItemTestFixture.GROUP, reloadedDto.getGroup());
    assertEquals(PcrBatchItemTestFixture.CREATED_BY, reloadedDto.getCreatedBy());
    assertEquals(PcrBatchItemTestFixture.RESULT, reloadedDto.getResult());
  }

  @Test
  public void updatePcrBatchItem_onSuccess_PcrBatchItemUpdated()
      throws ResourceGoneException, ResourceNotFoundException {
    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    UUID pcrBatchItemUuid = createWithRepository(newDto, pcrBatchItemRepository::onCreate);

    JsonApiDocument pcrBatchToUpdate = JsonApiDocuments.createJsonApiDocument(pcrBatchItemUuid,
      PcrBatchItemDto.TYPENAME, Map.of("result", "newResult"));
    pcrBatchItemRepository.onUpdate(pcrBatchToUpdate, pcrBatchItemUuid);

    PcrBatchItemDto reloadedDto = pcrBatchItemRepository.getOne(pcrBatchItemUuid, "").getDto();
    assertEquals("newResult", reloadedDto.getResult());
  }

  @Test
  public void deletePcrReaction_onSuccess_PcrReactionDeleted()
      throws ResourceGoneException, ResourceNotFoundException {
    PcrBatchItemDto newDto = PcrBatchItemTestFixture.newPcrBatchItem();
    UUID pcrBatchItemUuid = createWithRepository(newDto, pcrBatchItemRepository::onCreate);
    pcrBatchItemRepository.onDelete(pcrBatchItemUuid);
    assertThrows(ResourceNotFoundException.class, () -> pcrBatchItemRepository.getOne(
      pcrBatchItemUuid, ""
    ));
  }
}
