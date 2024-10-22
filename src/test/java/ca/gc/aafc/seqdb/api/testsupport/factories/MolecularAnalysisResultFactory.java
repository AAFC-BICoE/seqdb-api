package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;

import java.util.UUID;

public class MolecularAnalysisResultFactory {

  public static MolecularAnalysisResult.MolecularAnalysisResultBuilder newMolecularAnalysisResult() {

    return MolecularAnalysisResult.builder()
      .uuid(UUID.randomUUID())
      .group("dina")
      .createdBy("test user");
  }

}
