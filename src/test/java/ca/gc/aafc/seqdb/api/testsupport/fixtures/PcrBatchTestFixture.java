package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;

public class PcrBatchTestFixture {

  private static final String GROUP = "aafc";
  private static final String CREATED_BY = "createdBy";

  public static PcrBatchDto newPcrBatch() {
    PcrBatchDto pcrBatchDto = new PcrBatchDto();
    pcrBatchDto.setGroup(GROUP);
    pcrBatchDto.setCreatedBy(CREATED_BY);
    return pcrBatchDto;
  }
  
}
