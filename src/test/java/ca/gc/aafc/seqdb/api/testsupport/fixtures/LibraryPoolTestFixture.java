package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class LibraryPoolTestFixture {
  public static final String GROUP = "aafc";

  public static LibraryPoolDto newLibraryPool(){
    LibraryPoolDto dto = new LibraryPoolDto();
    dto.setGroup(GROUP);
    dto.setName(TestableEntityFactory.generateRandomName(8));
    return dto;
  }
}

