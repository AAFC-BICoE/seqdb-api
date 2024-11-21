package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchItemDto;

public class MetagenomicsBatchItemTestFixture {

  public static MetagenomicsBatchItemDto newMetagenomicsBatchItem() {
    return MetagenomicsBatchItemDto.builder()
      .createdBy("test-user")
      .build();
  }
}
