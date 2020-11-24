package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet.IndexSetBuilder;

public class IndexSetFactory implements TestableEntityFactory<IndexSet> {

  @Override
  public IndexSet getEntityInstance() {
    return newIndexSet().build();
  }
  
  public static IndexSet.IndexSetBuilder newIndexSet() {
    return IndexSet.builder()
        .name(TestableEntityFactory.generateRandomName(10));
  }

  public static List<IndexSet> newListOf(int qty) {
    return newListOf(qty, null);
  }

  public static List<IndexSet> newListOf(int qty,
      BiFunction<IndexSet.IndexSetBuilder, Integer, IndexSet.IndexSetBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, IndexSetFactory::newIndexSet, configuration,
        IndexSetBuilder::build);
  }

}
