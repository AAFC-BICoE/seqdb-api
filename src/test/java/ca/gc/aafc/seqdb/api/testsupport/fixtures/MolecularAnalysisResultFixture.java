package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisResultDto;

public class MolecularAnalysisResultFixture {

  public static final String GROUP = "aafc";

  public static MolecularAnalysisResultDto newMolecularAnalysisResult() {
    return MolecularAnalysisResultDto.builder()
      .group(GROUP)
      .createdBy("test-user")
      .build();
  }
}
