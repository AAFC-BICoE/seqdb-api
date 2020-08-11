package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer.PcrPrimerBuilder;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer.PrimerType;

public class PcrPrimerFactory implements TestableEntityFactory<PcrPrimer> {
  
  @Override
  public PcrPrimer getEntityInstance() {
    return newPcrPrimer().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrPrimer.PcrPrimerBuilder newPcrPrimer() {
    
    return PcrPrimer.builder()
        .type(PrimerType.PRIMER)
        .name(TestableEntityFactory.generateRandomName(10))
        .seq("CTTGGTCATTTAGAGGAAGTAA")
        .direction("F")
        .lotNumber(1)
        .uuid(UUID.randomUUID());
    
  }  
  
  /**
   * A utility method to create a list of qty number of PcrPrimers with no configuration.
   * 
   * @param qty The number of PcrPrimers populated in the list
   * @return List of PcrPrimer
   */
  public static List<PcrPrimer> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of PcrPrimer with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of PcrPrimer that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of PcrPrimer
   */
  public static List<PcrPrimer> newListOf(int qty,
      BiFunction<PcrPrimer.PcrPrimerBuilder, Integer, PcrPrimer.PcrPrimerBuilder> configuration) {
    
    return TestableEntityFactory.newEntity(qty, PcrPrimerFactory::newPcrPrimer, configuration,
        PcrPrimerBuilder::build);
  }

}
