package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;

public class PreLibraryPrepTestFixture {

  public static final String GROUP = "aafc";

  public static PreLibraryPrepDto newPreLibraryPrep() {
    PreLibraryPrepDto dto = new PreLibraryPrepDto();
    dto.setGroup(GROUP);
    dto.setPreLibraryPrepType(PreLibraryPrep.PreLibraryPrepType.SIZE_SELECTION);
    dto.setNotes("my note");
    dto.setConcentration(8.3);
    return dto;
  }

}
