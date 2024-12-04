package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatch;

public final class MetagenomicsBatchFactory {

  private MetagenomicsBatchFactory() {
    // static utility class
  }

  public static MetagenomicsBatch.MetagenomicsBatchBuilder newMetagenomicsBatch() {

    return MetagenomicsBatch.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .name(TestableEntityFactory.generateRandomName(10))
      .group("dina");
  }
}
