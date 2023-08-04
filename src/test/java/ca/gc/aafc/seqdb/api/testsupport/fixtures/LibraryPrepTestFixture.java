package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;

public class LibraryPrepTestFixture {

  public static final String GROUP = "aafc";

  public static final UUID MATERIAL_SAMPLE_UUID = UUID.randomUUID();

  public static LibraryPrepDto newLibraryPrep() {
    LibraryPrepDto dto = new LibraryPrepDto();
    dto.setGroup(GROUP);
    dto.setSize("test size");

    dto.setMaterialSample(
      ExternalRelationDto.builder().id(MATERIAL_SAMPLE_UUID.toString()).type("material-sample")
        .build());

    return dto;
  }

  public static ExternalRelationDto newMaterialSampleExternalRelationship() {
    return ExternalRelationDto.builder().id(UUID.randomUUID().toString()).type("material-sample")
      .build();
  }
}
