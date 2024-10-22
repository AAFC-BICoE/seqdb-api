package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class MolecularAnalysisRunTestFixture {

  public static final String GROUP = "aafc";

  public static MolecularAnalysisRunDto newMolecularAnalysisRun() {
    return MolecularAnalysisRunDto.builder()
      .group(GROUP)
      .name(TestableEntityFactory.generateRandomName(8))
      .createdBy("test-user")
      .build();
  }
}
