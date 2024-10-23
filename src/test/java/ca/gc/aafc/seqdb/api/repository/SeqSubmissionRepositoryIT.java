package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqSubmissionTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequencingFacilityTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.PathSpec;
import io.crnk.core.queryspec.QuerySpec;
import java.util.List;
import javax.inject.Inject;

public class SeqSubmissionRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private SequencingFacilityRepository sequencingFacilityRepository;

  @Inject
  private SeqSubmissionRepository seqSubmissionRepositoryRepository;

  public SeqSubmissionDto setupSeqSubmission() {

    SequencingFacilityDto facility =
      sequencingFacilityRepository.create(SequencingFacilityTestFixture.newSequencingFacility());
    SeqSubmissionDto seqSubmissionDto = SeqSubmissionTestFixture.newSeqSubmission();
    seqSubmissionDto.setSequencingFacility(facility);

    return seqSubmissionRepositoryRepository
            .create(seqSubmissionDto);
  }

  @Test
  public void createSeqSubmission_onSuccess_SeqReactionCreated() {
    SeqSubmissionDto seqSubmissionDto = setupSeqSubmission();
    assertNotNull(seqSubmissionDto.getUuid());
    assertNotNull(seqSubmissionDto.getSequencingFacility());
  }

  @Test
  public void updateSeqReaction_onSuccess_SeqReactionUpdated() {
    SeqSubmissionDto seqSubmissionDto = setupSeqSubmission();
    assertNotNull(seqSubmissionDto.getUuid());

    SeqSubmissionDto found = seqSubmissionRepositoryRepository.findOne(
      seqSubmissionDto.getUuid(),
            new QuerySpec(SeqSubmissionDto.class)
    );

    found.setName("Updated name");
    seqSubmissionRepositoryRepository.save(found);

    QuerySpec querySpec = new QuerySpec(SeqSubmissionDto.class);
    querySpec.setIncludedRelations(List.of(new IncludeRelationSpec(PathSpec.of("seqBatch"))));

    SeqSubmissionDto updated = seqSubmissionRepositoryRepository.findOne(
      seqSubmissionDto.getUuid(), querySpec);

    assertEquals("Updated name", updated.getName());
  }
}
