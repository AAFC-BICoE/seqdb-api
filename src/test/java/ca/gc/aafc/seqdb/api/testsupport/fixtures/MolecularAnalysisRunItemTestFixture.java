package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;

public class MolecularAnalysisRunItemTestFixture {

  public static MolecularAnalysisRunItemDto newMolecularAnalysisRunItem() {
    return MolecularAnalysisRunItemDto.builder()
      .createdBy("test-user")
      .build();
  }
}
