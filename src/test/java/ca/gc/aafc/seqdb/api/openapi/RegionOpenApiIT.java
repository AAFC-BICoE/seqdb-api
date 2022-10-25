package ca.gc.aafc.seqdb.api.openapi;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.RegionTestFixture;
import lombok.SneakyThrows;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@Transactional
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class RegionOpenApiIT extends BaseRestAssuredTest {

  public static final String TYPE_NAME = "region";

  protected RegionOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void region_SpecValid() {
    RegionDto region = RegionTestFixture.newRegion();

    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "Region",
      sendPost(TYPE_NAME, JsonAPITestHelper.toJsonAPIMap(TYPE_NAME, JsonAPITestHelper.toAttributeMap(region),
        null,
        null)
      ).extract().asString());
  }
}
