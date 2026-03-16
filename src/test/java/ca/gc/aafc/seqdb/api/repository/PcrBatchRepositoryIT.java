package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.RegionTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.ThermocyclerProfileTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class PcrBatchRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private PcrBatchRepository pcrBatchRepository;

  @Inject
  private PcrPrimerRepository pcrPrimerRepository;

  @Inject
  private RegionRepository regionRepository;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;

  @Test
  public void createAndFindPcrBatch_pcrBatchCreatedAndReturned()
      throws ResourceGoneException, ResourceNotFoundException {

    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    UUID primerForwardUuid = createWithRepository(PcrPrimerTestFixture.newForwardPrimer(), pcrPrimerRepository::onCreate);
    UUID primerReverseUuid = createWithRepository(PcrPrimerTestFixture.newReversePrimer(), pcrPrimerRepository::onCreate);

    UUID regionUuid = createWithRepository(RegionTestFixture.newRegion(), regionRepository::onCreate);
    UUID thermocyclerProfileUuid = createWithRepository(ThermocyclerProfileTestFixture.newThermocyclerProfile(),
      thermocyclerProfileRepository::onCreate);

    JsonApiDocument pcrBatchToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, PcrBatchDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(newDto),
        Map.of(
          "primerForward", JsonApiDocument.ResourceIdentifier.builder().id(primerForwardUuid)
            .type(PcrPrimerDto.TYPENAME).build(),
          "primerReverse", JsonApiDocument.ResourceIdentifier.builder().id(primerReverseUuid)
            .type(PcrPrimerDto.TYPENAME).build(),
          "region", JsonApiDocument.ResourceIdentifier.builder().id(regionUuid)
            .type(RegionDto.TYPENAME).build(),
          "thermocyclerProfile", JsonApiDocument.ResourceIdentifier.builder().id(thermocyclerProfileUuid)
            .type(ThermocyclerProfileDto.TYPENAME).build(),
          "storageUnit", JsonApiDocument.ResourceIdentifier.builder().id(PcrBatchTestFixture.STORAGE_UNIT_UUID)
            .type("storage-unit").build()
          ));

    UUID pcrBatchUuid = createWithRepository(pcrBatchToCreate, pcrBatchRepository::onCreate);
    PcrBatchDto reloadedDto = pcrBatchRepository.getOne(pcrBatchUuid, "include=" +
      String.join(",", pcrBatchToCreate.getRelationships().keySet())).getDto();

    assertNotNull(reloadedDto);
    assertEquals(newDto.getGroup(), reloadedDto.getGroup());
    assertEquals(primerForwardUuid, reloadedDto.getPrimerForward().getUuid());
    assertEquals(primerReverseUuid, reloadedDto.getPrimerReverse().getUuid());
    assertEquals(regionUuid, reloadedDto.getRegion().getUuid());
    assertEquals(thermocyclerProfileUuid, reloadedDto.getThermocyclerProfile().getUuid());

    assertEquals(PcrBatchTestFixture.CREATED_BY, reloadedDto.getCreatedBy());
    assertEquals(PcrBatchTestFixture.THERMOCYCLER, reloadedDto.getThermocycler());
    assertEquals(PcrBatchTestFixture.OBJECTIVE, reloadedDto.getObjective());
    assertEquals(PcrBatchTestFixture.POSITIVE_CONTROL, reloadedDto.getPositiveControl());
    assertEquals(PcrBatchTestFixture.REACTION_VOLUME, reloadedDto.getReactionVolume());
    assertEquals(PcrBatchTestFixture.REACTION_DATE, reloadedDto.getReactionDate());

    assertEquals(PcrBatchTestFixture.STORAGE_UNIT_UUID.toString(), reloadedDto.getStorageUnit().getId());
  }

  @Test
  public void updatePcrBatch_onSuccess_PcrBatchUpdated()
      throws ResourceGoneException, ResourceNotFoundException {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();
    UUID pcrBatchUuid = createWithRepository(newDto, pcrBatchRepository::onCreate);

    JsonApiDocument pcrBatchToUpdate = JsonApiDocuments.createJsonApiDocument(pcrBatchUuid,
      ProductDto.TYPENAME, Map.of("name", "updatedName"));
    pcrBatchRepository.onUpdate(pcrBatchToUpdate, pcrBatchUuid);
    PcrBatchDto reloadedDto = pcrBatchRepository.getOne(pcrBatchUuid, "").getDto();
    assertEquals("updatedName", reloadedDto.getName());
  }

  @Test
  public void deletePcrBatch_onSuccess_PcrBatchDeleted()
      throws ResourceGoneException, ResourceNotFoundException {
    PcrBatchDto newDto = PcrBatchTestFixture.newPcrBatch();

    UUID pcrBatchUuid = createWithRepository(newDto, pcrBatchRepository::onCreate);
    pcrBatchRepository.onDelete(pcrBatchUuid);
    assertThrows(ResourceNotFoundException.class, () -> pcrBatchRepository.getOne(
      pcrBatchUuid, ""
    ));
  }
}
