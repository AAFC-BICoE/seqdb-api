package ca.gc.aafc.seqdb.api.openapi;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPIRelationship;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.dina.testsupport.specs.ValidationRestrictionOptions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class PcrBatchOpenApiIT extends BaseRestAssuredTest {

  protected PcrBatchOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void pcrBatch_SpecValid() {

    PcrPrimerDto forwardPcrPrimerDto = PcrPrimerTestFixture.newPcrPrimer();
    forwardPcrPrimerDto.setDirection("F");
    PcrPrimerDto reversePcrPrimerDto = PcrPrimerTestFixture.newPcrPrimer();
    reversePcrPrimerDto.setDirection("R");

    String forwardPrimerUuid = JsonAPITestHelper.extractId(
            sendPost(PcrPrimerDto.TYPENAME,
                    JsonAPITestHelper.toJsonAPIMap(PcrPrimerDto.TYPENAME, JsonAPITestHelper.toAttributeMap(forwardPcrPrimerDto))));
    String reversePrimerUuid = JsonAPITestHelper.extractId(
            sendPost(PcrPrimerDto.TYPENAME,
                    JsonAPITestHelper.toJsonAPIMap(PcrPrimerDto.TYPENAME, JsonAPITestHelper.toAttributeMap(reversePcrPrimerDto))));

    PcrBatchDto pcrBatch = PcrBatchTestFixture.newPcrBatch();
    // clear external relationships
    pcrBatch.setExperimenters(null);
    pcrBatch.setAttachment(null);
    pcrBatch.setStorageUnit(null);
    pcrBatch.setStorageUnitType(null);
    pcrBatch.setProtocol(null);

    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "PcrBatch",
            sendPost(PcrBatchDto.TYPENAME, JsonAPITestHelper.toJsonAPIMap(PcrBatchDto.TYPENAME, JsonAPITestHelper.toAttributeMap(pcrBatch),
                    JsonAPITestHelper.toRelationshipMap(
                            List.of(JsonAPIRelationship.of("primerForward", PcrPrimerDto.TYPENAME, forwardPrimerUuid),
                                    JsonAPIRelationship.of("primerReverse", PcrPrimerDto.TYPENAME, reversePrimerUuid),
                                    JsonAPIRelationship.of("protocol", "protocol", UUID.randomUUID().toString()))),
                    null)
            ).extract().asString(),
            ValidationRestrictionOptions.builder().allowAdditionalFields(true).build()); //the spec is not complete yet
  }
}
