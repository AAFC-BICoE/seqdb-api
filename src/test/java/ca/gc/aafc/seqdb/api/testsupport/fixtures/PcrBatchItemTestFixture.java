package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.sanger.PcrBatchItemDto;

public class PcrBatchItemTestFixture {
  
  public static final String GROUP = "aafc";
  public static final String CREATED_BY = "created_by";
  public static final Integer WELL_COLUMN = 7;
  public static final String WELL_ROW = "C";

  public static PcrBatchItemDto newPcrBatchItem() {
    PcrBatchItemDto pcrBatchItemDto = new PcrBatchItemDto();
    pcrBatchItemDto.setGroup(GROUP);
    pcrBatchItemDto.setCreatedBy(CREATED_BY);;
    pcrBatchItemDto.setWellColumn(WELL_COLUMN);
    pcrBatchItemDto.setWellRow(WELL_ROW);;
    return pcrBatchItemDto;
  }

}
