package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqReactionTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

public class SeqReactionRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private SeqReactionRepository seqReactionRepository;

  @Inject
  private SeqBatchRepository seqBatchRepository;

  @Inject
  private PcrBatchItemRepository pcrBatchItemRepository;

  @Inject
  private PcrPrimerRepository pcrPrimerRepository;

  @Test
  public void createSeqReaction_onSuccess_SeqReactionCreated() {

    SeqReactionDto seqReaction = SeqReactionTestFixture.newSeqReaction();

    UUID seqBatchUuid = createWithRepository(SeqBatchTestFixture.newSeqBatch(), seqBatchRepository::onCreate);
    UUID pcrBatchItemUuid = createWithRepository(PcrBatchItemTestFixture.newPcrBatchItem(), pcrBatchItemRepository::onCreate);
    UUID seqPrimerUuid = createWithRepository(PcrPrimerTestFixture.newPcrPrimer(), pcrPrimerRepository::onCreate);

    JsonApiDocument seqReactionToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, SeqReactionDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(seqReaction),
        Map.of(
          "seqBatch", JsonApiDocument.ResourceIdentifier.builder().id(seqBatchUuid)
            .type(SeqBatchDto.TYPENAME).build(),
          "pcrBatchItem", JsonApiDocument.ResourceIdentifier.builder().id(pcrBatchItemUuid)
            .type(PcrBatchItemDto.TYPENAME).build(),
          "seqPrimer", JsonApiDocument.ResourceIdentifier.builder().id(seqPrimerUuid)
            .type(PcrPrimerDto.TYPENAME).build()
        ));

    seqReactionRepository.onCreate(seqReactionToCreate);
  }

  @Test
  public void updateSeqReaction_onSuccess_SeqReactionUpdated()
      throws ResourceGoneException, ResourceNotFoundException {
    SeqReactionDto seqReaction = SeqReactionTestFixture.newSeqReaction();
    UUID seqPrimerUuid = createWithRepository(PcrPrimerTestFixture.newPcrPrimer(), pcrPrimerRepository::onCreate);

    JsonApiDocument seqReactionToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, SeqReactionDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(seqReaction),
        Map.of(
          "seqPrimer", JsonApiDocument.ResourceIdentifier.builder().id(seqPrimerUuid)
            .type(PcrPrimerDto.TYPENAME).build()
        ));
    UUID seqReactionrUuid = createWithRepository(seqReactionToCreate, seqReactionRepository::onCreate);

    JsonApiDocument seqPrimerToUpdate = JsonApiDocuments.createJsonApiDocument(seqPrimerUuid,
      PcrPrimerDto.TYPENAME, Map.of("name", "Updated Primer"));
    pcrPrimerRepository.onUpdate(seqPrimerToUpdate, seqPrimerUuid);

    SeqReactionDto updated = seqReactionRepository.getOne(seqReactionrUuid, "include=seqPrimer").getDto();
    assertEquals("Updated Primer", updated.getSeqPrimer().getName());
  }
}
