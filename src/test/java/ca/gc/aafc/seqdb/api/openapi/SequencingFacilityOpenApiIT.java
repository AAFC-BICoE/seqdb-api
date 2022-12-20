package ca.gc.aafc.seqdb.api.openapi;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SequencingFacilityTestFixture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        classes = SeqdbApiLauncher.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class SequencingFacilityOpenApiIT extends BaseRestAssuredTest {

  protected SequencingFacilityOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void sequencingFacility_SpecValid() {
    SequencingFacilityDto sequencingFacility = SequencingFacilityTestFixture.newSequencingFacility();

    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "SequencingFacility",
            sendPost(SequencingFacilityDto.TYPENAME, JsonAPITestHelper.toJsonAPIMap(SequencingFacilityDto.TYPENAME, JsonAPITestHelper.toAttributeMap(sequencingFacility),
                    null,
                    null)
            ).extract().asString());
  }
}
