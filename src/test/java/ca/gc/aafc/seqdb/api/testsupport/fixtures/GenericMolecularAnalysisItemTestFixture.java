package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisItemDto;

public class GenericMolecularAnalysisItemTestFixture {

  public static GenericMolecularAnalysisItemDto newGenericMolecularAnalysisItem() {
    return GenericMolecularAnalysisItemDto.builder()
      .createdBy("test-user")
      .build();
  }
}
