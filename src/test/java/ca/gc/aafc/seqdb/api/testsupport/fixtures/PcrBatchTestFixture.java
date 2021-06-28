package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;

public class PcrBatchTestFixture {

  private static final String GROUP = "aafc";

  public static PcrBatchDto newPcrBatch() {
    PcrBatchDto pcrBatchDto = new PcrBatchDto();
    pcrBatchDto.setGroup(GROUP);
    pcrBatchDto.setCreatedBy("createdBy");
    return pcrBatchDto;
  }
  
}
