package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep.LibraryPrepBuilder;

public class LibraryPrepFactory implements TestableEntityFactory<LibraryPrep> {

  @Override
  public LibraryPrep getEntityInstance() {
    return newLibraryPrep().build();
  }

  public static LibraryPrep.LibraryPrepBuilder newLibraryPrep() {
    return LibraryPrep.builder()
        .libraryPrepBatch(LibraryPrepBatchFactory.newLibraryPrepBatch().build())
        .molecularSample(MolecularSampleFactory.newMolecularSample().build());
  }
  
  public static List<LibraryPrep> newListOf(int qty) {
    return newListOf(qty, null);
  }
  
  public static List<LibraryPrep> newListOf(
      int qty,
      BiFunction<LibraryPrep.LibraryPrepBuilder, Integer, LibraryPrep.LibraryPrepBuilder> configuration
  ) {
    return TestableEntityFactory.newEntity(qty, LibraryPrepFactory::newLibraryPrep, configuration,
        LibraryPrepBuilder::build);
  }

}
