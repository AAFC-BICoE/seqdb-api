package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.seqdb.api.entities.SequencingFacility;

import java.util.UUID;

public class SequencingFacilityFactory implements TestableEntityFactory<SequencingFacility> {

  @Override
  public SequencingFacility getEntityInstance() {
    return newSequencingFacility().build();
  }

  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @return Pre-configured builder with all mandatory fields set
   */
  public static SequencingFacility.SequencingFacilityBuilder newSequencingFacility() {
    return SequencingFacility.builder()
            .uuid(UUID.randomUUID())
            .createdBy("test user")
            .group("dina")
            .name("Facility");
  }

}
