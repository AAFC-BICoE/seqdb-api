package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqSubmissionTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequencingFacilityTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.crnk.core.queryspec.QuerySpec;
import javax.inject.Inject;

public class SeqSubmissionRepositoryIT extends BaseRepositoryTest {

  @Inject
  private SequencingFacilityRepository sequencingFacilityRepository;

  @Inject
  private SeqSubmissionRepository seqSubmissionRepositoryRepository;

  @Inject
  private SeqBatchRepository seqBatchRepository;

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

    SeqBatchDto seqBatch = SeqBatchTestFixture.newSeqBatch();
    seqBatchRepository.create(seqBatch);

    found.setName("Updated name");
    found.setSeqBatch(seqBatch);

    SeqSubmissionDto updated = seqSubmissionRepositoryRepository.save(found);
    assertEquals("Updated name", updated.getName());
    assertEquals(seqBatch.getUuid(), updated.getSeqBatch().getUuid());
  }
  
}
