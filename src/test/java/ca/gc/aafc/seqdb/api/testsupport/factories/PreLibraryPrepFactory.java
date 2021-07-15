package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepBuilder;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepType;

public class PreLibraryPrepFactory implements TestableEntityFactory<PreLibraryPrep> {

  @Override
  public PreLibraryPrep getEntityInstance() {
    return newPreLibraryPrep().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PreLibraryPrep.PreLibraryPrepBuilder newPreLibraryPrep() {
    return PreLibraryPrep.builder()
        .group("dina")
        .preLibraryPrepType(PreLibraryPrepType.SHEARING);
  }
  
  /**
   * A utility method to create a list of qty number of PreLibraryPrep with no configuration.
   * 
   * @param qty The number of PreLibraryPrep populated in the list
   * @return List of PreLibraryPrep
   */
  public static List<PreLibraryPrep> newListOf(int qty) {
    return newListOf(qty, null);
  }
  
  /**
   * A utility method to create a list of qty number of entities with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of entities that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of PreLibraryPrep
   */
  public static List<PreLibraryPrep> newListOf(int qty,
      BiFunction<PreLibraryPrep.PreLibraryPrepBuilder, Integer, PreLibraryPrep.PreLibraryPrepBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, PreLibraryPrepFactory::newPreLibraryPrep, configuration,
        PreLibraryPrepBuilder::build);
  }

}
