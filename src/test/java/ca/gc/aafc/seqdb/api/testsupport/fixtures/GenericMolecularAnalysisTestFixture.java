package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class GenericMolecularAnalysisTestFixture {

  public static final String GROUP = "aafc";

  public static GenericMolecularAnalysisDto newGenericMolecularAnalysis() {
    return GenericMolecularAnalysisDto.builder()
      .group(GROUP)
      .name(TestableEntityFactory.generateRandomName(12))
      .analysisType("HRMS")
      .createdBy("test-user")
      .build();
  }
}
