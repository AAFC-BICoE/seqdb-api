package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;


public class GenericMolecularAnalysisItemFactory {

  public static GenericMolecularAnalysisItem.GenericMolecularAnalysisItemBuilder newMolecularAnalysisRunItem(GenericMolecularAnalysis genericMolecularAnalysis) {

    return GenericMolecularAnalysisItem.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .genericMolecularAnalysis(genericMolecularAnalysis);
  }
}
