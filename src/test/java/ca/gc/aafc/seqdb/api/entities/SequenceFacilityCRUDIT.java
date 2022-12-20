package ca.gc.aafc.seqdb.api.entities;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.testsupport.factories.SequencingFacilityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SequenceFacilityCRUDIT extends SequenceModuleBaseIT {

  private static final String EXPECTED_NAME = "name";
  private static final String EXPECTED_GROUP = "DINA GROUP";
  private static final String EXPECTED_CREATED_BY = "createdBy";

  @Test
  void create() {
    SequencingFacility seqFacility = buildExpectedSequenceFacility();

    sequencingFacilityService.create(seqFacility);

    Assertions.assertNotNull(seqFacility.getId());
    Assertions.assertNotNull(seqFacility.getCreatedOn());
    Assertions.assertNotNull(seqFacility.getUuid());
  }

  @Test
  void find() {
    SequencingFacility seqFacility = buildExpectedSequenceFacility();

    sequencingFacilityService.create(seqFacility);

    SequencingFacility result = sequencingFacilityService.findOne(seqFacility.getUuid(), SequencingFacility.class);

    Assertions.assertEquals(EXPECTED_NAME, result.getName());
    Assertions.assertEquals(EXPECTED_GROUP, result.getGroup());
    Assertions.assertEquals(EXPECTED_CREATED_BY, result.getCreatedBy());
  }

  private SequencingFacility buildExpectedSequenceFacility() {
    return SequencingFacilityFactory.newSequencingFacility()
            .name(EXPECTED_NAME)
            .group(EXPECTED_GROUP)
            .createdBy(EXPECTED_CREATED_BY)
            .build();

  }
}
