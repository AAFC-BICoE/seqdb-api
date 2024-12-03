package ca.gc.aafc.seqdb.api.service;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.entities.QualityControl;
import ca.gc.aafc.seqdb.api.testsupport.factories.QualityControlFactory;

public class QualityControlServiceIT extends SequenceModuleBaseIT {

  @Test
  public void onValidEntity_save_noException() {
    QualityControl qc = QualityControlFactory.newQualityControl().build();
    qualityControlService.create(qc);
  }
}
