package ca.gc.aafc.seqdb.api.openapi;

import java.util.Map;

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
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.RegionTestFixture;
import lombok.SneakyThrows;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@Transactional
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class PcrPrimerOpenApiIT extends BaseRestAssuredTest {

  public static final String TYPE_NAME = "pcr-primer";

  protected PcrPrimerOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void pcrPrimer_SpecValid() {
    RegionDto regionDto = RegionTestFixture.newRegion();
    PcrPrimerDto pcrPrimerDto = PcrPrimerTestFixture.newPcrPrimer();
    pcrPrimerDto.setRegion(null);

    String uuid = sendPost("region", JsonAPITestHelper.toJsonAPIMap("region", JsonAPITestHelper.toAttributeMap(regionDto)))
      .extract()
      .response()
      .body()
      .path("data.id");
    
    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "PcrPrimer",
      sendPost(TYPE_NAME, JsonAPITestHelper.toJsonAPIMap(TYPE_NAME, JsonAPITestHelper.toAttributeMap(pcrPrimerDto),
        Map.of(
          "region", getRelationType("region", uuid)),
        null)
      ).extract().asString());
  }

  private Map<String, Object> getRelationType(String type, String uuid) {
    return Map.of("data", Map.of(
      "id", uuid,
      "type", type));
  }
  
}