package ca.gc.aafc.seqdb.api.entities;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.testsupport.factories.IndexSetFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.MetagenomicsBatchFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.MetagenomicsBatchItemFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.NgsIndexFactory;

public class MetagenomicsBatchIT extends SequenceModuleBaseIT {

  @Test
  public void onCreateMetagenomicsBatchEntities_entitiesSaved() {
    MetagenomicsBatch mgb = MetagenomicsBatchFactory.newMetagenomicsBatch()
      .indexSet(IndexSetFactory.newIndexSet().build())
      .build();
    metagenomicsBatchService.create(mgb);

    MetagenomicsBatchItem mgbi = MetagenomicsBatchItemFactory.newMetagenomicsBatchItem(mgb)
      .indexI5(NgsIndexFactory.newNgsIndex().build())
      .indexI7(NgsIndexFactory.newNgsIndex().build())
      .build();

    metagenomicsBatchItemService.create(mgbi);
  }
}

