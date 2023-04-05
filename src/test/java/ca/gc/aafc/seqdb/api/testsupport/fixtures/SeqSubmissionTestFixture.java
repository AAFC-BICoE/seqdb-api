package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.dina.testsupport.factories.TestableEntityFactory;
import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;

public class SeqSubmissionTestFixture {

  private static final String GROUP = "aafc";
  private static final String CREATED_BY = "createdBy";

  public static SeqSubmissionDto newSeqSubmission() {
    SeqSubmissionDto seqSubmissionDto = new SeqSubmissionDto();
    seqSubmissionDto.setName(TestableEntityFactory.generateRandomName(10));
    seqSubmissionDto.setGroup(GROUP);
    seqSubmissionDto.setCreatedBy(CREATED_BY);

    return seqSubmissionDto;
  }

}
