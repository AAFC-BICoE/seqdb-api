package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Container;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.Group;

public class ContainerFactory implements TestableEntityFactory<Container> {

  @Override
  public Container getEntityInstance() {
    return newContainer().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static Container.ContainerBuilder newContainer() {
    
    return Container.builder()
        .containerType(ContainerTypeFactory.newContainerType().build())
        .containerNumber(TestableEntityFactory.generateRandomName(10))
        .group(new Group(Group.PUBLIC_GROUP_NAME));

  }
    
  /**
   * A utility method to create a list of qty number of Containers with no configuration.
   * 
   * @param qty The number of Containers populated in the list
   * @return List of Container
   */
  public static List<Container> newListOf(int qty) {
    return newListOf(qty, null);
  }

  /**
   * A utility method to create a list of qty number of Container with an incrementing attribute
   * based on the configuration argument. An example of configuration would be the functional
   * interface (bldr, index) -> bldr.name(" string" + index)
   * 
   * @param qty           The number of Container that is populated in the list.
   * @param configuration the function to apply, usually to differentiate the different entities in
   *                      the list.
   * @return List of Container
   */
  public static List<Container> newListOf(int qty,
      BiFunction<Container.ContainerBuilder, Integer, Container.ContainerBuilder> configuration) {
    
    return TestableEntityFactory.newEntity(qty, ContainerFactory::newContainer, configuration,
        Container.ContainerBuilder::build);
  }
  
}
