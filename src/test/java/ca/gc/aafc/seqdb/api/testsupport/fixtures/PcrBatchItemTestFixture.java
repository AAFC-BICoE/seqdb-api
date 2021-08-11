package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.PcrBatchItemDto;

public class PcrBatchItemTestFixture {
  
  public static final String GROUP = "aafc";
  public static final String CREATED_BY = "created_by";

  public static PcrBatchItemDto newPcrBatchItem() {
    PcrBatchItemDto pcrBatchItemDto = new PcrBatchItemDto();
    pcrBatchItemDto.setGroup(GROUP);
    pcrBatchItemDto.setCreatedBy(CREATED_BY);;
    return pcrBatchItemDto;
  }

}
