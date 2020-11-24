package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;

public class StepTemplateFactory implements TestableEntityFactory<StepTemplate> {
  
  @Override
  public StepTemplate getEntityInstance() {
    return newStepTemplate().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static StepTemplate.StepTemplateBuilder newStepTemplate() {
    StepResourceValue inputs[] = StepResourceValue.values();
    StepResourceValue outputs[] = StepResourceValue.values();
    return StepTemplate.builder()
            .name(TestableEntityFactory.generateRandomName(10))
            .inputs(inputs)
            .outputs(outputs);
    
  }
    
  /**
   * A utility method to create a list of qty number of StepTemplates with no configuration.
   * 
   * @param qty The number of StepTemplates populated in the list
   * @return List of StepTemplate
   */
  public static List<StepTemplate> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of StepTemplate with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of StepTemplate that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of StepTemplate
   */
  public static List<StepTemplate> newListOf(int qty,
      BiFunction<StepTemplate.StepTemplateBuilder, Integer, StepTemplate.StepTemplateBuilder> configuration) {
    
    return TestableEntityFactory.newEntity(qty, StepTemplateFactory::newStepTemplate, configuration,
        StepTemplate.StepTemplateBuilder::build);
  }
  

}
