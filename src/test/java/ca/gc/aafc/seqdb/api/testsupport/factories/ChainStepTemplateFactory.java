package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.workflow.ChainStepTemplate;

public class ChainStepTemplateFactory implements TestableEntityFactory<ChainStepTemplate> {

  @Override
  public ChainStepTemplate getEntityInstance() {
    return newChainStepTemplate().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static ChainStepTemplate.ChainStepTemplateBuilder newChainStepTemplate() {

    return ChainStepTemplate.builder()
        .chainTemplate(ChainTemplateFactory.newChainTemplate().build())
        .stepTemplate(StepTemplateFactory.newStepTemplate().build())
        .stepNumber(1);

  }

  /**
   * A utility method to create a list of qty number of ChainStepTemplatees with no configuration.
   * 
   * @param qty
   *          The number of PcrPrimers populated in the list
   * @return List of PcrPrimer
   */
  public static List<ChainStepTemplate> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of ChainStepTemplate with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty
   *          The number of ChainStepTemplate that is populated in the list.
   * @param configuration
   *          the function to apply, usually to differentiate the different entities in the list.
   * @return List of ChainStepTemplate
   */
  public static List<ChainStepTemplate> newListOf(int qty,
      BiFunction<ChainStepTemplate.ChainStepTemplateBuilder, Integer, ChainStepTemplate.ChainStepTemplateBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, ChainStepTemplateFactory::newChainStepTemplate, configuration,
        ChainStepTemplate.ChainStepTemplateBuilder::build);
  }
  

}
