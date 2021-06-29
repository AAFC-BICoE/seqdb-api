package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class PcrBatchTestFixture {

  private static final String GROUP = "aafc";

  public static PcrBatchDto newPcrBatch() {
    PcrBatchDto pcrBatchDto = new PcrBatchDto();
    pcrBatchDto.setGroup(GROUP);
    pcrBatchDto.setCreatedBy("createdBy");
    pcrBatchDto.setName(TestableEntityFactory.generateRandomName(10));
    return pcrBatchDto;
  }
  
}
