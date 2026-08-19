package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.exception.ConflictException;
import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequencingFacilityTestFixture;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SequencingFacilityRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private SequencingFacilityRepository sequencingFacilityRepository;

  public UUID setupSequencingFacility() {
    SequencingFacilityDto dto = SequencingFacilityTestFixture.newSequencingFacility();
    return createWithRepository(dto, sequencingFacilityRepository::onCreate);
  }

  @Test
  public void createSeqReaction_onSuccess_SeqReactionCreated()
      throws ResourceGoneException, ResourceNotFoundException {
    UUID sequencingFacilityUuid = setupSequencingFacility();
    SequencingFacilityDto dto = sequencingFacilityRepository.getOne(sequencingFacilityUuid, "").getDto();
      assertNotNull(dto.getUuid());
  }

  @Test
  public void updateSeqReaction_onSuccess_SeqReactionUpdated()
      throws ResourceGoneException, ResourceNotFoundException, ConflictException {
    UUID sequencingFacilityUuid = setupSequencingFacility();

    JsonApiDocument sequencingFacilityToUpdate = JsonApiDocuments.createJsonApiDocument(sequencingFacilityUuid,
      SequencingFacilityDto.TYPENAME, Map.of("name", "Updated name"));
    sequencingFacilityRepository.onUpdate(sequencingFacilityToUpdate, sequencingFacilityUuid);

    SequencingFacilityDto reloadedDto = sequencingFacilityRepository.getOne(sequencingFacilityUuid, "").getDto();
    assertEquals("Updated name", reloadedDto.getName());
  }
  
}
