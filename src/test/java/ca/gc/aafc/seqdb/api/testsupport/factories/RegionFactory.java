package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Region;
import ca.gc.aafc.seqdb.api.entities.Region.RegionBuilder;

public class RegionFactory implements TestableEntityFactory<Region> {
  
  @Override
  public Region getEntityInstance() {
    return newRegion().build();
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static Region.RegionBuilder newRegion() {
    
    return Region.builder()
        .name(TestableEntityFactory.generateRandomName(10))
        .symbol("TEST");
    
  }
  
  /**
   * A utility method to create a list of qty number of Regions with no configuration.
   * 
   * @param qty The number of Regions populated in the list
   * @return List of Region
   */
  public static List<Region> newListOf(int qty) {
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
   * @return List of Region
   */
  public static List<Region> newListOf(int qty,
      BiFunction<Region.RegionBuilder, Integer, Region.RegionBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, RegionFactory::newRegion, configuration,
        RegionBuilder::build);
  }

}
