package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent.LibraryPoolContentBuilder;

public class LibraryPoolContentFactory implements TestableEntityFactory<LibraryPoolContent> {

  @Override
  public LibraryPoolContent getEntityInstance() {
    return newLibraryPoolContent().build();
  }

  public static LibraryPoolContent.LibraryPoolContentBuilder newLibraryPoolContent() {
    return LibraryPoolContent.builder()
        .libraryPool(LibraryPoolFactory.newLibraryPool().build())
        .pooledLibraryPrepBatch(LibraryPrepBatchFactory.newLibraryPrepBatch().build());
  }

  public static List<LibraryPoolContent> newListOf(int qty) {
    return newListOf(qty, null);
  }

  public static List<LibraryPoolContent> newListOf(int qty,
      BiFunction<LibraryPoolContent.LibraryPoolContentBuilder, Integer, LibraryPoolContent.LibraryPoolContentBuilder> configuration) {
    return TestableEntityFactory.newEntity(qty, LibraryPoolContentFactory::newLibraryPoolContent, configuration,
        LibraryPoolContentBuilder::build);
  }

}
