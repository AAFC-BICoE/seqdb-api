package ca.gc.aafc.seqdb.api.entities;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.testsupport.factories.GenericMolecularAnalysisFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.GenericMolecularAnalysisItemFactory;

public class GenericMolecularAnalysisIT extends SequenceModuleBaseIT {

  @Test
  public void onGenericMolecularAnalysisEntities_entitiesSaved() {

    GenericMolecularAnalysis gma = GenericMolecularAnalysisFactory
      .newGenericMolecularAnalysis()
      .build();
    genericMolecularAnalysisService.create(gma);

    GenericMolecularAnalysisItem gmai = GenericMolecularAnalysisItemFactory
      .newMolecularAnalysisRunItem(gma)
      .build();
    genericMolecularAnalysisItemService.create(gmai);

  }
}
