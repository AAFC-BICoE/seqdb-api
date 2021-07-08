package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile.ThermocycleProfileBuilder;

public class ThermocyclerProfileFactory implements TestableEntityFactory<ThermocyclerProfile> {

  @Override
  public ThermocyclerProfile getEntityInstance() {
    return newThermocycleProfile().build();
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @param group Group to be set on the {@link ThermocyclerProfile}
   * @return Pre-configured builder with all mandatory fields set
   */
  public static ThermocyclerProfile.ThermocycleProfileBuilder newThermocycleProfile() {
    
    return ThermocyclerProfile.builder()
        .name(TestableEntityFactory.generateRandomName(10));
    
  }  
  
  /**
   * A utility method to create a list of qty number of PcrProfile with no configuration.
   * 
   * @param qty The number of PcrProfile populated in the list
   * @return List of PcrProfile
   */
  public static List<ThermocyclerProfile> newListOf(int qty) {
        
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
  public static List<ThermocyclerProfile> newListOf(int qty,
      BiFunction<ThermocyclerProfile.ThermocycleProfileBuilder, Integer, ThermocyclerProfile.ThermocycleProfileBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, ThermocyclerProfileFactory::newThermocycleProfile, configuration,
      ThermocycleProfileBuilder::build);

  }

}
