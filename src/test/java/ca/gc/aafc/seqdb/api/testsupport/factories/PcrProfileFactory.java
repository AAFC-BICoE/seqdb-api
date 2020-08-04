package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.List;
import java.util.function.BiFunction;

import ca.gc.aafc.seqdb.api.entities.Group;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import ca.gc.aafc.seqdb.api.entities.PcrProfile.PcrProfileBuilder;

public class PcrProfileFactory implements TestableEntityFactory<PcrProfile> {

  @Override
  public PcrProfile getEntityInstance() {
    return newPcrProfile().build();
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * 
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrProfile.PcrProfileBuilder newPcrProfile() {
    
    return newPcrProfile(null);    
  }
  
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @param group Group to be set on the {@link PcrProfile}
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrProfile.PcrProfileBuilder newPcrProfile(Group group) {
    
    return PcrProfile.builder()
        .name(TestableEntityFactory.generateRandomName(10))
        .group(group);
    
  }  
  
  /**
   * A utility method to create a list of qty number of PcrProfile with no configuration.
   * 
   * @param qty The number of PcrProfile populated in the list
   * @return List of PcrProfile
   */
  public static List<PcrProfile> newListOf(int qty) {
        
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
  public static List<PcrProfile> newListOf(int qty,
      BiFunction<PcrProfile.PcrProfileBuilder, Integer, PcrProfile.PcrProfileBuilder> configuration) {

    return TestableEntityFactory.newEntity(qty, PcrProfileFactory::newPcrProfile, configuration,
        PcrProfileBuilder::build);

  }

}
