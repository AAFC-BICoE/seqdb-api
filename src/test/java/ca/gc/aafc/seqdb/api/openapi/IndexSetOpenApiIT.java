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
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.IndexSetTestFixture;
import lombok.SneakyThrows;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class IndexSetOpenApiIT extends BaseRestAssuredTest {

  protected IndexSetOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void indexSet_SpecValid() {
    IndexSetDto indexSet = IndexSetTestFixture.newIndexSet();

    OpenAPI3Assertions.assertRemoteSchema(
      OpenAPIConstants.SEQDB_API_SPECS_URL,
      "IndexSet",
      sendPost(
        "index-set", 
        JsonAPITestHelper.toJsonAPIMap(
          "index-set", 
          JsonAPITestHelper.toAttributeMap(indexSet),
          null,
          null
        )
      ).extract().asString(),
      ValidationRestrictionOptions.builder().allowableMissingFields(Set.of("ngsIndexes")).build()
    );
  }
}
