package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class LibraryPoolContentTestFixture {

  public static final String GROUP = "aafc";

  /**
   *     return LibraryPoolContent.builder()
   *         .libraryPool(LibraryPoolFactory.newLibraryPool().build())
   *         .pooledLibraryPrepBatch(LibraryPrepBatchFactory.newLibraryPrepBatch().build());
   * @return
   */

  public static LibraryPoolContentDto newLibraryPoolContent(){
    LibraryPoolContentDto dto = new LibraryPoolContentDto();
    return dto;
  }
}
