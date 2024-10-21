package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequencingFacilityTestFixture;
import io.crnk.core.queryspec.QuerySpec;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SequencingFacilityRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private SequencingFacilityRepository sequencingFacilityRepository;

  public SequencingFacilityDto setupSequencingFacility() {
    return sequencingFacilityRepository
            .create(SequencingFacilityTestFixture.newSequencingFacility());
  }

  @Test
  public void createSeqReaction_onSuccess_SeqReactionCreated() {
    SequencingFacilityDto sequencingFacilityDto = setupSequencingFacility();
    assertNotNull(sequencingFacilityDto.getUuid());
  }

  @Test
  public void updateSeqReaction_onSuccess_SeqReactionUpdated() {
    SequencingFacilityDto sequencingFacilityDto = setupSequencingFacility();
    assertNotNull(sequencingFacilityDto.getUuid());

    SequencingFacilityDto found = sequencingFacilityRepository.findOne(
            sequencingFacilityDto.getUuid(),
            new QuerySpec(SequencingFacilityDto.class)
    );

    found.setName("Updated name");

    SequencingFacilityDto updated = sequencingFacilityRepository.save(found);
    assertEquals("Updated name", updated.getName());
  }
  
}
