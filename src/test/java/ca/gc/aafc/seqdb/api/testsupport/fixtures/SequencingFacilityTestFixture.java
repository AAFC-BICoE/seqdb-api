package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class SequencingFacilityTestFixture {

  private static final String GROUP = "aafc";
  private static final String CREATED_BY = "createdBy";

  public static SequencingFacilityDto newSequencingFacility() {
    SequencingFacilityDto sequencingFacilityDto = new SequencingFacilityDto();
    sequencingFacilityDto.setGroup(GROUP);
    sequencingFacilityDto.setCreatedBy(CREATED_BY);
    sequencingFacilityDto.setName(TestableEntityFactory.generateRandomNameLettersOnly(8));
    return sequencingFacilityDto;
  }

}
