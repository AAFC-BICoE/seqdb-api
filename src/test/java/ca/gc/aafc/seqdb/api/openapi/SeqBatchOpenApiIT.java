package ca.gc.aafc.seqdb.api.openapi;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPIRelationship;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.dina.testsupport.specs.ValidationRestrictionOptions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.RegionTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.ThermocyclerProfileTestFixture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class SeqBatchOpenApiIT extends BaseRestAssuredTest {

  protected SeqBatchOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void seqBatch_SpecValid() {

    RegionDto region = RegionTestFixture.newRegion();
    ThermocyclerProfileDto thermocyclerProfileDto = ThermocyclerProfileTestFixture.newThermocyclerProfile();

    String regionUuid = JsonAPITestHelper.extractId(
            sendPost(RegionDto.TYPENAME,
                    JsonAPITestHelper.toJsonAPIMap(RegionDto.TYPENAME, JsonAPITestHelper.toAttributeMap(region))));

    String thermocyclerProfileUuid = JsonAPITestHelper.extractId(
            sendPost(ThermocyclerProfileDto.TYPENAME,
                    JsonAPITestHelper.toJsonAPIMap(ThermocyclerProfileDto.TYPENAME, JsonAPITestHelper.toAttributeMap(thermocyclerProfileDto))));

    SeqBatchDto seqBatch = SeqBatchTestFixture.newSeqBatch();
    // clear external relationships
    seqBatch.setExperimenters(null);
    seqBatch.setStorageUnit(null);
    seqBatch.setStorageUnitType(null);

    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "SeqBatch",
            sendPost(SeqBatchDto.TYPENAME, JsonAPITestHelper.toJsonAPIMap(SeqBatchDto.TYPENAME, JsonAPITestHelper.toAttributeMap(seqBatch),
                    JsonAPITestHelper.toRelationshipMap(
                            List.of(JsonAPIRelationship.of("region", RegionDto.TYPENAME, regionUuid),
                                    JsonAPIRelationship.of("thermocyclerProfile", ThermocyclerProfileDto.TYPENAME, thermocyclerProfileUuid),
                                    JsonAPIRelationship.of("protocol", "protocol", UUID.randomUUID().toString()),
                                    JsonAPIRelationship.of("storageUnit", "storage-unit", UUID.randomUUID().toString()))),
                    null)
            ).extract().asString(),
            ValidationRestrictionOptions.builder().allowableMissingFields(Set.of("experimenters", "storageUnitType")).build());
  }
}
