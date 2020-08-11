package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate;

public class StepResourceFactory implements TestableEntityFactory<StepResource> {
  @Override
  public StepResource getEntityInstance() {
    return newStepResource().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static StepResource.StepResourceBuilder newStepResource() {

    StepResource.StepResourceBuilder builder = StepResource.builder()
          .value(StepTemplate.StepResourceValue.PRODUCT)
          .chainStepTemplate(ChainStepTemplateFactory.newChainStepTemplate().build())
          .product(ProductFactory.newProduct().build());
      
    return builder;    
  }  
    
  /**
   * A utility method to create a list of qty number of StepResources with no configuration.
   * 
   * @param qty The number of StepResources populated in the list
   * @return List of StepResource
   */
  public static List<StepResource> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of StepResource with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of StepResource that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of StepResource
   */
  public static List<StepResource> newListOf(int qty,
      BiFunction<StepResource.StepResourceBuilder, Integer, StepResource.StepResourceBuilder> configuration) {
    
    return TestableEntityFactory.newEntity(qty, StepResourceFactory::newStepResource, configuration,
        StepResource.StepResourceBuilder::build);
  }
}
