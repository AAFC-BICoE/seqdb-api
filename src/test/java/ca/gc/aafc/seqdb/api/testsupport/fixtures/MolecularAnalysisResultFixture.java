package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisResultDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;

import java.util.List;
import java.util.Map;

public class MolecularAnalysisResultFixture {

  public static final String GROUP = "aafc";

  public static MolecularAnalysisResultDto newMolecularAnalysisResult() {
    return MolecularAnalysisResultDto.builder()
      .group(GROUP)
      .createdBy("test-user")
      .tabularResults(new MolecularAnalysisResult.TabularResults(
        List.of(Map.of("record_id", "test-record", "region", 1))))
      .build();
  }
}
