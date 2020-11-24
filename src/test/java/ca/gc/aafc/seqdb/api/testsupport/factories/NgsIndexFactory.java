package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex.NgsIndexBuilder;

public class NgsIndexFactory implements TestableEntityFactory<NgsIndex> {

  @Override
  public NgsIndex getEntityInstance() {
    return newNgsIndex().build();
  }

  public static NgsIndex.NgsIndexBuilder newNgsIndex() {
    IndexSet set = IndexSet.builder().name(TestableEntityFactory.generateRandomName(10)).build();

    return NgsIndex.builder().name(TestableEntityFactory.generateRandomName(10)).indexSet(set);
  }

  public static List<NgsIndex> newListOf(int qty) {
    return newListOf(qty, null);
  }

  public static List<NgsIndex> newListOf(int qty,
      BiFunction<NgsIndex.NgsIndexBuilder, Integer, NgsIndex.NgsIndexBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, NgsIndexFactory::newNgsIndex, configuration, NgsIndexBuilder::build);
  }

}
