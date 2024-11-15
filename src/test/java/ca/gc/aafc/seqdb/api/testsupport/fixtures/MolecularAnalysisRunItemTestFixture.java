package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;

public class MolecularAnalysisRunItemTestFixture {

  public static MolecularAnalysisRunItemDto newMolecularAnalysisRunItem() {
    return MolecularAnalysisRunItemDto.builder()
      .createdBy("test-user")
      .usageType(SeqReactionDto.TYPENAME)
      .build();
  }
}
