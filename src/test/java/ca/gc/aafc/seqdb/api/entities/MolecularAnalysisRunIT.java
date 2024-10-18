package ca.gc.aafc.seqdb.api.entities;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisResultFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisRunFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisRunItemFactory;

/**
 * Tests for all MolecularAnalysisRun-based entities.
 */
public class MolecularAnalysisRunIT extends SequenceModuleBaseIT {

  @Test
  public void onCreateMolecularAnalysisEntities_entitiesSaved() {
    MolecularAnalysisRun mar = MolecularAnalysisRunFactory.newMolecularAnalysisRun()
      .build();
    molecularAnalysisRunService.create(mar);

    MolecularAnalysisResult maResult = MolecularAnalysisResultFactory
      .newMolecularAnalysisResult().build();
    molecularAnalysisResultService.create(maResult);

    MolecularAnalysisRunItem mari = MolecularAnalysisRunItemFactory
      .newMolecularAnalysisRunItem(mar)
      .molecularAnalysisResult(maResult)
      .build();
    molecularAnalysisRunItemService.create(mari);

  }
}
