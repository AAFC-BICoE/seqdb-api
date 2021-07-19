package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch.LibraryPrepBatchBuilder;

public class LibraryPrepBatchFactory implements TestableEntityFactory<LibraryPrepBatch> {

  @Override
  public LibraryPrepBatch getEntityInstance() {
    return newLibraryPrepBatch().build();
  }

  public static LibraryPrepBatch.LibraryPrepBatchBuilder newLibraryPrepBatch() {
    return LibraryPrepBatch.builder()
      .group("dina")
      .name(TestableEntityFactory.generateRandomName(10));
  }

  public static List<LibraryPrepBatch> newListOf(int qty) {
    return newListOf(qty, null);
  }

  public static List<LibraryPrepBatch> newListOf(int qty,
      BiFunction<LibraryPrepBatch.LibraryPrepBatchBuilder, Integer, LibraryPrepBatch.LibraryPrepBatchBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, LibraryPrepBatchFactory::newLibraryPrepBatch, configuration,
        LibraryPrepBatchBuilder::build);
  }

}
