package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Group;
import ca.gc.aafc.seqdb.api.entities.ReactionComponent;
import ca.gc.aafc.seqdb.api.entities.ReactionComponent.ReactionComponentBuilder;

public class ReactionComponentFactory implements TestableEntityFactory<ReactionComponent> {

  @Override
  public ReactionComponent getEntityInstance() {
    return newReactionComponent().build();
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static ReactionComponent.ReactionComponentBuilder newReactionComponent() {
    
    return newReactionComponent(null);
    
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @param group Group to be set on the {@link ReactionComponent}
   * @return Pre-configured builder with all mandatory fields set
   */
  public static ReactionComponent.ReactionComponentBuilder newReactionComponent(Group group) {

    ReactionComponent.ReactionComponentBuilder builder = ReactionComponent.builder() ;
      
    if(group!=null) {
      builder.protocol(ProtocolFactory.newProtocol(group).build());      
    }else {
      builder.protocol(ProtocolFactory.newProtocol().build());      
    }
    return builder;
  }  
  /**
   * A utility method to create a list of qty number of ReactionComponents with no configuration.
   * 
   * @param qty The number of ReactionComponents populated in the list
   * @return List of ReactionComponent
   */
  public static List<ReactionComponent> newListOf(int qty) {
        
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
   * @return List of ReactionComponent
   */
  public static List<ReactionComponent> newListOf(int qty,
      BiFunction<ReactionComponent.ReactionComponentBuilder, Integer, ReactionComponent.ReactionComponentBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, ReactionComponentFactory::newReactionComponent,
        configuration, ReactionComponentBuilder::build);

  }
}
