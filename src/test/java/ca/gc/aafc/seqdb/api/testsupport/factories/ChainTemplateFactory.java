package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.workflow.ChainTemplate;

public class ChainTemplateFactory implements TestableEntityFactory<ChainTemplate> {

  @Override
  public ChainTemplate getEntityInstance() {
    return newChainTemplate().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static ChainTemplate.ChainTemplateBuilder newChainTemplate() {
    
    return ChainTemplate.builder()
            .name(TestableEntityFactory.generateRandomName(10));
    
  }
    
  /**
   * A utility method to create a list of qty number of ChainTemplates with no configuration.
   * 
   * @param qty The number of ChainTemplates populated in the list
   * @return List of ChainTemplate
   */
  public static List<ChainTemplate> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of ChainTemplate with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of ChainTemplate that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of ChainTemplate
   */
  public static List<ChainTemplate> newListOf(int qty,
      BiFunction<ChainTemplate.ChainTemplateBuilder, Integer, ChainTemplate.ChainTemplateBuilder> configuration) {
    
    return TestableEntityFactory.newEntity(qty, ChainTemplateFactory::newChainTemplate, configuration,
        ChainTemplate.ChainTemplateBuilder::build);
  }
  

}
