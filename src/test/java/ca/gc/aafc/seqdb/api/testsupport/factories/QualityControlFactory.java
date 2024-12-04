package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.dina.util.UUIDHelper;
import ca.gc.aafc.seqdb.api.entities.QualityControl;

public class QualityControlFactory {

  public static QualityControl.QualityControlBuilder newQualityControl() {
    return QualityControl.builder()
      .name(TestableEntityFactory.generateRandomName(7))
      .qcType("reserpine_standard")
      .uuid(UUIDHelper.generateUUIDv7())
      .group("dina")
      .createdBy("test user");
  }
}
