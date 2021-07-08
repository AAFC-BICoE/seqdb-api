package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class SeqBatchTestFixture {

  private static final String GROUP = "aafc";
  private static final String CREATED_BY = "createdBy";

  public static SeqBatchDto newSeqBatch() {
    SeqBatchDto seqBatchDto = new SeqBatchDto();
    seqBatchDto.setGroup(GROUP);
    seqBatchDto.setCreatedBy(CREATED_BY);
    seqBatchDto.setName(TestableEntityFactory.generateRandomName(10));
    return seqBatchDto;
  }
  
}

