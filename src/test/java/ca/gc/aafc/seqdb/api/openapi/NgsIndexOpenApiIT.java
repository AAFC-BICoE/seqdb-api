package ca.gc.aafc.seqdb.api.openapi;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.dina.testsupport.specs.ValidationRestrictionOptions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.NgsIndexDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.NgsIndexTestFixture;
import lombok.SneakyThrows;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class NgsIndexOpenApiIT extends BaseRestAssuredTest {

  protected NgsIndexOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void ngsIndex_SpecValid() {
    NgsIndexDto ngsIndex = NgsIndexTestFixture.newNgsIndex();

    OpenAPI3Assertions.assertRemoteSchema(
      OpenAPIConstants.SEQDB_API_SPECS_URL,
      "NgsIndex",
      sendPost(
        NgsIndexDto.TYPENAME, 
        JsonAPITestHelper.toJsonAPIMap(
          NgsIndexDto.TYPENAME, 
          JsonAPITestHelper.toAttributeMap(ngsIndex),
          null,
          null
        )
      ).extract().asString(),
      ValidationRestrictionOptions.builder().allowableMissingFields(Set.of("indexSet")).build()
    );
  }
}
