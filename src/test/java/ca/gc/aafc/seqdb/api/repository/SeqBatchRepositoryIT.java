package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.dto.external.ProtocolExternalDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.RegionTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.ThermocyclerProfileTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class SeqBatchRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private RegionRepository regionRepository;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;

  @Inject
  private SeqBatchRepository seqBatchRepository;

  private static final UUID TEST_PROTOCOL_UUID = UUID.randomUUID();

  @Test
  public void createSeqBatch_onSuccess_SeqBatchCreated() throws ResourceGoneException, ResourceNotFoundException {

    UUID regionUuid = createWithRepository(RegionTestFixture.newRegion(), regionRepository::onCreate);
    UUID tpUuid = createWithRepository(ThermocyclerProfileTestFixture.newThermocyclerProfile(), thermocyclerProfileRepository::onCreate);

    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    JsonApiDocument seqBatchToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, SeqBatchDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(newDto),
        Map.of(
          "region", JsonApiDocument.ResourceIdentifier.builder().id(regionUuid)
            .type(RegionDto.TYPENAME).build(),
          "thermocyclerProfile", JsonApiDocument.ResourceIdentifier.builder().id(tpUuid)
            .type(ThermocyclerProfileDto.TYPENAME).build(),
          "protocol", JsonApiDocument.ResourceIdentifier.builder().id(TEST_PROTOCOL_UUID)
            .type(ProtocolExternalDto.EXTERNAL_TYPENAME).build()
        ));

    UUID seqBatchUuid = createWithRepository(seqBatchToCreate, seqBatchRepository::onCreate);

    SeqBatchDto reloadedDto = seqBatchRepository.getOne(seqBatchUuid, "include=" +
      String.join(",", seqBatchToCreate.getRelationships().keySet())).getDto();

    assertEquals(newDto.getGroup(), reloadedDto.getGroup());
    assertEquals(regionUuid, reloadedDto.getRegion().getUuid());
    assertEquals(tpUuid, reloadedDto.getThermocyclerProfile().getUuid());
    assertEquals(TEST_PROTOCOL_UUID.toString(), reloadedDto.getProtocol().getId());
  }

  @Test
  public void updateSeqBatch_onSuccess_PcrBatchUpdated()
      throws ResourceGoneException, ResourceNotFoundException {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    UUID seqBatchItemUuid = createWithRepository(newDto, seqBatchRepository::onCreate);

    JsonApiDocument seqBatchToUpdate = JsonApiDocuments.createJsonApiDocument(seqBatchItemUuid,
      SeqBatchDto.TYPENAME, Map.of("name", "updatedName"));
    seqBatchRepository.onUpdate(seqBatchToUpdate, seqBatchItemUuid);

    SeqBatchDto updated = seqBatchRepository.getOne(seqBatchItemUuid, "").getDto();
    assertEquals("updatedName", updated.getName());
  }

  @Test
  public void deleteSeqBatch_onSuccess_SeqBatchDeleted()
      throws ResourceGoneException, ResourceNotFoundException {
    SeqBatchDto newDto = SeqBatchTestFixture.newSeqBatch();
    UUID seqBatchItemUuid = createWithRepository(newDto, seqBatchRepository::onCreate);
    seqBatchRepository.onDelete(seqBatchItemUuid);
    assertThrows(ResourceNotFoundException.class, () -> seqBatchRepository.getOne(
      seqBatchItemUuid, ""
    ));
  }
}
