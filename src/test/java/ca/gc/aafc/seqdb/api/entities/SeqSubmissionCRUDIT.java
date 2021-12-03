package ca.gc.aafc.seqdb.api.entities;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.testsupport.factories.SeqSubmissionFactory;

public class SeqSubmissionCRUDIT extends SequenceModuleBaseIT {

  private static final String EXPECTED_NAME = "name";
  private static final String EXPECTED_GROUP = "DINA GROUP";
  private static final String EXPECTED_CREATED_BY = "createdBy";
  private static final UUID EXPECTED_SUBMITTED_BY = UUID.randomUUID();

  @Test
  void create() {
    SeqSubmission seqSubmission = buildExpectedSeqSubmission();

    seqSubmissionService.create(seqSubmission);

    Assertions.assertNotNull(seqSubmission.getId());
    Assertions.assertNotNull(seqSubmission.getCreatedOn());
    Assertions.assertNotNull(seqSubmission.getUuid());
  }

  @Test
  void find() {
    SeqSubmission seqSubmission = buildExpectedSeqSubmission();

    seqSubmissionService.create(seqSubmission);

    SeqSubmission result = seqSubmissionService.findOne(seqSubmission.getUuid(), SeqSubmission.class);

    Assertions.assertEquals(EXPECTED_NAME, result.getName());
    Assertions.assertEquals(EXPECTED_GROUP, result.getGroup());
    Assertions.assertEquals(EXPECTED_CREATED_BY, result.getCreatedBy());
    Assertions.assertEquals(EXPECTED_SUBMITTED_BY, result.getSubmittedBy());
  }

  private SeqSubmission buildExpectedSeqSubmission() {
    return SeqSubmissionFactory.newSeqSubmission()
      .name(EXPECTED_NAME)
      .group(EXPECTED_GROUP)
      .createdBy(EXPECTED_CREATED_BY)
      .submittedBy(EXPECTED_SUBMITTED_BY)
      .build();

  }
  
}
