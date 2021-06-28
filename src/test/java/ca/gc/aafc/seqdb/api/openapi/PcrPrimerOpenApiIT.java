package ca.gc.aafc.seqdb.api.openapi;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.http.client.utils.URIBuilder;
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

  private static final String SPEC_HOST = "raw.githubusercontent.com";
  private static final String SPEC_PATH = "DINA-Web/sequence-specs/support_19623_Create_OpenAPI3_file_for_PcrPrimer_and_Region/schema/sequence.yml";
  private static final URIBuilder URI_BUILDER = new URIBuilder();

  public static final String TYPE_NAME = "pcrPrimer";

  static {
    URI_BUILDER.setScheme("https");
    URI_BUILDER.setHost(SPEC_HOST);
    URI_BUILDER.setPath(SPEC_PATH);
  }

  protected PcrPrimerOpenApiIT() {
    super("/api");
  }

  public static URL getOpenAPISpecsURL() throws URISyntaxException, MalformedURLException {
    return URI_BUILDER.build().toURL();
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
    
    OpenAPI3Assertions.assertRemoteSchema(getOpenAPISpecsURL(), "PcrPrimer",
      sendPost(TYPE_NAME, JsonAPITestHelper.toJsonAPIMap(TYPE_NAME, JsonAPITestHelper.toAttributeMap(pcrPrimerDto),
        Map.of(
          "region", getRelationType("region", uuid)),
        null)
      ).extract().asString(), true);
  }

  private Map<String, Object> getRelationType(String type, String uuid) {
    return Map.of("data", Map.of(
      "id", uuid,
      "type", type));
  }
  
}