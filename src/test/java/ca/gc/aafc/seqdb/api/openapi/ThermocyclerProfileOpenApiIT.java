package ca.gc.aafc.seqdb.api.openapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
// import ca.gc.aafc.dina.testsupport.specs.ValidationRestrictionOptions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.ThermocyclerProfileTestFixture;
import lombok.SneakyThrows;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class ThermocyclerProfileOpenApiIT extends BaseRestAssuredTest {

  protected ThermocyclerProfileOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void thermocyclerProfile_SpecValid() {
    ThermocyclerProfileDto thermocyclerProfile = ThermocyclerProfileTestFixture.newThermocyclerProfile();

    OpenAPI3Assertions.assertRemoteSchema(
      OpenAPIConstants.SEQDB_API_SPECS_URL,
      "ThermocyclerProfile",
      sendPost(
        ThermocyclerProfileDto.TYPENAME, 
        JsonAPITestHelper.toJsonAPIMap(
            ThermocyclerProfileDto.TYPENAME, 
          JsonAPITestHelper.toAttributeMap(thermocyclerProfile),
          null,
          null
        )
      ).extract().asString()
    );
  }
}
