package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.ContainerType;

public class ContainerTypeFactory implements TestableEntityFactory<ContainerType> {

  @Override
  public ContainerType getEntityInstance() {
    return newContainerType().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static ContainerType.ContainerTypeBuilder newContainerType() {
    
    return ContainerType.builder()
        .name(TestableEntityFactory.generateRandomName(10))
        .numberOfColumns(5)
        .numberOfRows(6);
  }
    
  /**
   * A utility method to create a list of qty number of ContainerTypes with no configuration.
   * 
   * @param qty The number of ContainerTypes populated in the list
   * @return List of ContainerType
   */
  public static List<ContainerType> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of ContainerType with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of ContainerType that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of ContainerType
   */
  public static List<ContainerType> newListOf(int qty,
      BiFunction<ContainerType.ContainerTypeBuilder, Integer, ContainerType.ContainerTypeBuilder> configuration) {
    
    return TestableEntityFactory.newEntity(qty, ContainerTypeFactory::newContainerType, configuration,
        ContainerType.ContainerTypeBuilder::build);
  }
  
}
