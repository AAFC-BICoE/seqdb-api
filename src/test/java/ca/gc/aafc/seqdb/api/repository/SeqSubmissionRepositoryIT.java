package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqSubmissionTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequencingFacilityTestFixture;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;

public class SeqSubmissionRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private SequencingFacilityRepository sequencingFacilityRepository;

  @Inject
  private SeqSubmissionRepository seqSubmissionRepositoryRepository;

  public UUID setupSeqSubmission() {

    SequencingFacilityDto dto = SequencingFacilityTestFixture.newSequencingFacility();
    UUID sequencingFacilityId = createWithRepository(dto, sequencingFacilityRepository::onCreate);

    SeqSubmissionDto seqSubmissionDto = SeqSubmissionTestFixture.newSeqSubmission();
    JsonApiDocument dtoToCreate =
      JsonApiDocuments.createJsonApiDocumentWithRelToOne(null, SeqSubmissionDto.TYPENAME,
        JsonAPITestHelper.toAttributeMap(seqSubmissionDto),
        Map.of("sequencingFacility", JsonApiDocument.ResourceIdentifier.builder().id(sequencingFacilityId)
          .type(SequencingFacilityDto.TYPENAME).build()));

    return createWithRepository(dtoToCreate, seqSubmissionRepositoryRepository::onCreate);
  }

  @Test
  public void createSeqSubmission_onSuccess_SeqReactionCreated()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID seqSubmissionUuid = setupSeqSubmission();

    SeqSubmissionDto dto = seqSubmissionRepositoryRepository.getOne(seqSubmissionUuid, "include=sequencingFacility").getDto();
    assertNotNull(dto.getUuid());
    assertNotNull(dto.getSequencingFacility());
  }

//  @Test
//  public void updateSeqReaction_onSuccess_SeqReactionUpdated() {
//    SeqSubmissionDto seqSubmissionDto = setupSeqSubmission();
//    assertNotNull(seqSubmissionDto.getUuid());
//
//    SeqSubmissionDto found = seqSubmissionRepositoryRepository.findOne(
//      seqSubmissionDto.getUuid(),
//            new QuerySpec(SeqSubmissionDto.class)
//    );
//
//    found.setName("Updated name");
//    seqSubmissionRepositoryRepository.save(found);
//
//    QuerySpec querySpec = new QuerySpec(SeqSubmissionDto.class);
//    SeqSubmissionDto updated = seqSubmissionRepositoryRepository.findOne(
//      seqSubmissionDto.getUuid(), querySpec);
//
//    assertEquals("Updated name", updated.getName());
//  }
}
