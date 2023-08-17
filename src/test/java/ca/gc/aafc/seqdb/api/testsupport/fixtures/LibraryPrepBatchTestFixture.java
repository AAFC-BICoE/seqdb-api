package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class LibraryPrepBatchTestFixture {

  public static final String GROUP = "aafc";

  public static LibraryPrepBatchDto newLibraryPrepBatch(){
    LibraryPrepBatchDto dto = new LibraryPrepBatchDto();
    dto.setGroup(GROUP);
    dto.setName(TestableEntityFactory.generateRandomName(8));

    return dto;
  }
}
