package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.QualityControlDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class QualityControlTestFixture {

  public static final String GROUP = "aafc";

  public static QualityControlDto newQualityControl() {
    return QualityControlDto.builder()
      .group(GROUP)
      .name(TestableEntityFactory.generateRandomName(11))
      .qcType("standard")
      .createdBy("test-user")
      .build();
  }
}
