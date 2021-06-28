package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class RegionTestFixture {
  
  public static RegionDto newRegion() {
    RegionDto regionDto = new RegionDto();
    regionDto.setName(TestableEntityFactory.generateRandomName(10));
    regionDto.setSymbol("TEST");
    regionDto.setDescription("description");
    
    return regionDto;
  }
}
