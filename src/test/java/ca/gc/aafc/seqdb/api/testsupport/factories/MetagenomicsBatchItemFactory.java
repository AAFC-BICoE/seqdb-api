package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatch;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatchItem;

import java.util.UUID;

public final class MetagenomicsBatchItemFactory {

  private MetagenomicsBatchItemFactory() {
    // static utility class
  }

  public static MetagenomicsBatchItem.MetagenomicsBatchItemBuilder newMetagenomicsBatchItem(
    MetagenomicsBatch metagenomicsBatch) {

    return MetagenomicsBatchItem.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .metagenomicsBatch(metagenomicsBatch);
  }
}


