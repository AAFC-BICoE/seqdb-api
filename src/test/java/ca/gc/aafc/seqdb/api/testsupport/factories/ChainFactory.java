package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.workflow.Chain;

public class ChainFactory implements TestableEntityFactory<Chain> {

  @Override
  public Chain getEntityInstance() {
    return newChain().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can
   * be further customized to return the actual entity object, call the
   * .build() method on a builder,with specified group passed on as parameter
   * @param group Group to be set on the {@link Chain}
   * @return Pre-configured builder with all mandatory fields set
   */  
  public static Chain.ChainBuilder newChain() {
    return  Chain.builder()
        .name(TestableEntityFactory.generateRandomName(10))
        .chainTemplate(ChainTemplateFactory.newChainTemplate().build());
   }  
    
  /**
   * A utility method to create a list of qty number of Chains with no configuration.
   * 
   * @param qty The number of Chains populated in the list
   * @return List of Chain
   */
  public static List<Chain> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of Chain with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of Chain that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of Chain
   */
  public static List<Chain> newListOf(int qty,
      BiFunction<Chain.ChainBuilder, Integer, Chain.ChainBuilder> configuration) {
    
    return TestableEntityFactory.newEntity(qty, ChainFactory::newChain, configuration,
        Chain.ChainBuilder::build);
  }
  
}
