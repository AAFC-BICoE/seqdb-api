package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;

public class PcrBatchItemTestFixture {
  
  public static final String GROUP = "aafc";
  public static final String CREATED_BY = "created_by";
  public static final String RESULT = "Good Band";

  public static PcrBatchItemDto newPcrBatchItem(PcrBatchDto batch) {
    PcrBatchItemDto pcrBatchItemDto = newPcrBatchItem();
    pcrBatchItemDto.setPcrBatch(batch);
    return pcrBatchItemDto;
  }

  public static PcrBatchItemDto newPcrBatchItem() {
    PcrBatchItemDto pcrBatchItemDto = new PcrBatchItemDto();
    pcrBatchItemDto.setGroup(GROUP);
    pcrBatchItemDto.setCreatedBy(CREATED_BY);
    pcrBatchItemDto.setResult(RESULT);
    return pcrBatchItemDto;
  }

}
