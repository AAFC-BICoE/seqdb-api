package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class MetagenomicsBatchTestFixture {

  public static final String GROUP = "aafc";

  public static MetagenomicsBatchDto newMetagenomicsBatch() {
    return MetagenomicsBatchDto.builder()
      .group(GROUP)
      .name(TestableEntityFactory.generateRandomName(12))
      .createdBy("test-user")
      .build();
  }
}
