package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Group;
import ca.gc.aafc.seqdb.api.entities.PcrReaction;
import ca.gc.aafc.seqdb.api.entities.PcrReaction.PcrReactionBuilder;

/**
 * Creates PcrBatch entities that are populated with all the required fields.
 */
public class PcrReactionFactory implements TestableEntityFactory<PcrReaction> {

  @Override
  public PcrReaction getEntityInstance() {
    return newPcrReaction().build();
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrReaction.PcrReactionBuilder newPcrReaction() {
    
    return newPcrReaction(null);    
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @param group Group to be set on the {@link PcrReaction}
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrReaction.PcrReactionBuilder newPcrReaction(Group group) {
    
    PcrReaction.PcrReactionBuilder bldr = PcrReaction.builder()
        .tubeNumber(0)
        .group(group);
    
    // only change the group of PcrReaction if a specific group is set, otherwise use the
    // default
    if (group != null) {
      bldr.pcrBatch(PcrBatchFactory.newPcrBatch(group).build());
    } else {
      bldr.pcrBatch(PcrBatchFactory.newPcrBatch().build());
    }
    return bldr;
  }
  
  /**
   * A utility method to create a list of qty number of PcrReactions with no configuration.
   * 
   * @param qty
   *          The number of PcrReaction populated in the list
   * @return List of PcrPrimer
   */
  public static List<PcrReaction> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of PcrReaction with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty
   *          The number of PcrReaction that is populated in the list.
   * @param configuration
   *          the function to apply, usually to differentiate the different entities in the list.
   * @return List of PcrReaction
   */
  public static List<PcrReaction> newListOf(int qty,
      BiFunction<PcrReaction.PcrReactionBuilder, Integer, PcrReaction.PcrReactionBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, PcrReactionFactory::newPcrReaction, configuration,
        PcrReactionBuilder::build);
  }

}
