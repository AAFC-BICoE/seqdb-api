package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;

public class MolecularAnalysisRunFactory {

  public static MolecularAnalysisRun.MolecularAnalysisRunBuilder newMolecularAnalysisRun() {

    return MolecularAnalysisRun.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .name(TestableEntityFactory.generateRandomName(10))
      .group("dina");
  }
}
