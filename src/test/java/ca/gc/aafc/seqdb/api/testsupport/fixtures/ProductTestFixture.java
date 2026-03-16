package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class ProductTestFixture {

  public static final String GROUP = "aafc";

  public static ProductDto newProduct() {
    return ProductDto.builder()
      .group(GROUP)
      .name(TestableEntityFactory.generateRandomName(11))
      .createdBy("test-user")
      .build();
  }
}
