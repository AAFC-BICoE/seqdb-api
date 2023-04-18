package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqSubmissionTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.crnk.core.queryspec.QuerySpec;
import javax.inject.Inject;

public class SeqSubmissionRepositoryIT extends BaseRepositoryTest {

  @Inject
  private SeqSubmissionRepository seqSubmissionRepositoryRepository;

  @Inject
  private SeqBatchRepository seqBatchRepository;

  public SeqSubmissionDto setupSeqSubmission() {
    return seqSubmissionRepositoryRepository
            .create(SeqSubmissionTestFixture.newSeqSubmission());
  }

  @Test
  public void createSeqSubmission_onSuccess_SeqReactionCreated() {
    SeqSubmissionDto seqSubmissionDto = setupSeqSubmission();
    assertNotNull(seqSubmissionDto.getUuid());
  }

  @Test
  public void updateSeqReaction_onSuccess_SeqReactionUpdated() {
    SeqSubmissionDto seqSubmissionDto = setupSeqSubmission();
    assertNotNull(seqSubmissionDto.getUuid());

    SeqSubmissionDto found = seqSubmissionRepositoryRepository.findOne(
      seqSubmissionDto.getUuid(),
            new QuerySpec(SequencingFacilityDto.class)
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
