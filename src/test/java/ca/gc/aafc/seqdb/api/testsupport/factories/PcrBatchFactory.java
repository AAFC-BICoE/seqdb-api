package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Group;
import ca.gc.aafc.seqdb.api.entities.PcrBatch;
import ca.gc.aafc.seqdb.api.entities.PcrBatch.PcrBatchBuilder;

/**
 * Creates PcrBatch entities that are populated with all the required fields.
 */
public class PcrBatchFactory implements TestableEntityFactory<PcrBatch> {

  @Override
  public PcrBatch getEntityInstance() {

    return newPcrBatch().build();

  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrBatch.PcrBatchBuilder newPcrBatch() {

    return newPcrBatch(null);
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @param group Group to be set on the {@link PcrBatch}
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrBatch.PcrBatchBuilder newPcrBatch(Group group) {

    return PcrBatch.builder()
        .name(TestableEntityFactory.generateRandomName(10))
        .type(PcrBatch.PcrBatchType.SANGER)
        .containerType(ContainerTypeFactory.newContainerType().build())
        .group(group);

  }
  

  /**
   * A utility method to create a list of qty number of PcrBatches with no configuration.
   * 
   * @param qty
   *          The number of PcrPrimers populated in the list
   * @return List of PcrPrimer
   */
  public static List<PcrBatch> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of PcrBatch with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty
   *          The number of PcrBatch that is populated in the list.
   * @param configuration
   *          the function to apply, usually to differentiate the different entities in the list.
   * @return List of PcrBatch
   */
  public static List<PcrBatch> newListOf(int qty,
      BiFunction<PcrBatch.PcrBatchBuilder, Integer, PcrBatch.PcrBatchBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, PcrBatchFactory::newPcrBatch, configuration,
        PcrBatchBuilder::build);
  }

}
