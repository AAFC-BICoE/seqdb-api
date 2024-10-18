package ca.gc.aafc.seqdb.api.entities;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisRunFactory;

public class MolecularAnalysisRunIT extends SequenceModuleBaseIT {

  @Test
  public void onCreateMolecularAnalysisRun_entitySaved() {
    MolecularAnalysisRun mar = MolecularAnalysisRunFactory.newMolecularAnalysisRun()
      .build();
    molecularAnalysisRunService.create(mar);
  }
}
