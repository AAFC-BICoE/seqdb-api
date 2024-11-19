package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;

public class GenericMolecularAnalysisFactory {

  public static GenericMolecularAnalysis.GenericMolecularAnalysisBuilder newGenericMolecularAnalysis() {

    return GenericMolecularAnalysis.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .name(TestableEntityFactory.generateRandomName(10))
      .analysisType("HRMS")
      .group("dina");
  }
}
