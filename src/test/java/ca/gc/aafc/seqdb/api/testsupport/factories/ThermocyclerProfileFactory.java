package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.ThermocycleProfile;
import ca.gc.aafc.seqdb.api.entities.ThermocycleProfile.ThermocycleProfileBuilder;

public class ThermocyclerProfileFactory implements TestableEntityFactory<ThermocycleProfile> {

  @Override
  public ThermocycleProfile getEntityInstance() {
    return newThermocycleProfile().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @param group Group to be set on the {@link ThermocycleProfile}
   * @return Pre-configured builder with all mandatory fields set
   */
  public static ThermocycleProfile.ThermocycleProfileBuilder newThermocycleProfile() {
    
    return ThermocycleProfile.builder()
        .name(TestableEntityFactory.generateRandomName(10));
    
  }  
  
  /**
   * A utility method to create a list of qty number of PcrProfile with no configuration.
   * 
   * @param qty The number of PcrProfile populated in the list
   * @return List of PcrProfile
   */
  public static List<ThermocycleProfile> newListOf(int qty) {
        
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
   * @return List of PcrProfiles
   */
  public static List<ThermocycleProfile> newListOf(int qty,
      BiFunction<ThermocycleProfile.ThermocycleProfileBuilder, Integer, ThermocycleProfile.ThermocycleProfileBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, ThermocyclerProfileFactory::newThermocycleProfile, configuration,
      ThermocycleProfileBuilder::build);

  }

}
