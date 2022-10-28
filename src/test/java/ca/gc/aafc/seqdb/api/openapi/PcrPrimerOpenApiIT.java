package ca.gc.aafc.seqdb.api.openapi;

import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPIRelationship;
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
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class PcrPrimerOpenApiIT extends BaseRestAssuredTest {

  protected PcrPrimerOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void pcrPrimer_SpecValid() {
    RegionDto regionDto = RegionTestFixture.newRegion();
    PcrPrimerDto pcrPrimerDto = PcrPrimerTestFixture.newPcrPrimer();
    pcrPrimerDto.setRegion(null);

    String uuid = JsonAPITestHelper.extractId(
            sendPost(RegionDto.TYPENAME, JsonAPITestHelper.toJsonAPIMap(RegionDto.TYPENAME, JsonAPITestHelper.toAttributeMap(regionDto))));
    
    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "PcrPrimer",
      sendPost(PcrPrimerDto.TYPENAME, JsonAPITestHelper.toJsonAPIMap(PcrPrimerDto.TYPENAME, JsonAPITestHelper.toAttributeMap(pcrPrimerDto),
                      JsonAPITestHelper.toRelationshipMap(JsonAPIRelationship.of(RegionDto.TYPENAME, RegionDto.TYPENAME, uuid)),
        null)
      ).extract().asString());
  }
}
