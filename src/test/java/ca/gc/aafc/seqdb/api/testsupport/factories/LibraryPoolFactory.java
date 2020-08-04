package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool.LibraryPoolBuilder;

public class LibraryPoolFactory implements TestableEntityFactory<LibraryPool> {

  @Override
  public LibraryPool getEntityInstance() {
    return newLibraryPool().build();
  }

  public static LibraryPool.LibraryPoolBuilder newLibraryPool() {
    return LibraryPool.builder().name(TestableEntityFactory.generateRandomName(10));
  }

  public static List<LibraryPool> newListOf(int qty) {
    return newListOf(qty, null);
  }

  public static List<LibraryPool> newListOf(int qty,
      BiFunction<LibraryPool.LibraryPoolBuilder, Integer, LibraryPool.LibraryPoolBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, LibraryPoolFactory::newLibraryPool, configuration,
        LibraryPoolBuilder::build);
  }

}
