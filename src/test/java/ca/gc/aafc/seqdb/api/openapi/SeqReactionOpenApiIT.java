package ca.gc.aafc.seqdb.api.openapi;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPIRelationship;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.dina.testsupport.specs.ValidationRestrictionOptions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrBatchItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqBatchTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.SeqReactionTestFixture;

import java.util.UUID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(
  classes = SeqdbApiLauncher.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class SeqReactionOpenApiIT extends BaseRestAssuredTest {

  protected SeqReactionOpenApiIT() {
    super("/api");
  }

  @SneakyThrows
  @Test
  void seqReaction_SpecValid() {

    PcrPrimerDto primerDto = PcrPrimerTestFixture.newPcrPrimer();
    String primerUuid = JsonAPITestHelper.extractId(
            sendPost(PcrPrimerDto.TYPENAME,
                    JsonAPITestHelper.toJsonAPIMap(PcrPrimerDto.TYPENAME, JsonAPITestHelper.toAttributeMap(primerDto))));

    PcrBatchItemDto pcrBatchItem = PcrBatchItemTestFixture.newPcrBatchItem();
    pcrBatchItem.setMaterialSample(null);
    String pcrBatchItemDtoUuid = JsonAPITestHelper.extractId(
            sendPost(PcrBatchItemDto.TYPENAME,
                    JsonAPITestHelper.toJsonAPIMap(PcrBatchItemDto.TYPENAME, JsonAPITestHelper.toAttributeMap(pcrBatchItem))));

    SeqBatchDto seqBatchDto = SeqBatchTestFixture.newSeqBatch();
    seqBatchDto.setExperimenters(null);
    String seqBatchDtoUuid = JsonAPITestHelper.extractId(
            sendPost(SeqBatchDto.TYPENAME,
                    JsonAPITestHelper.toJsonAPIMap(SeqBatchDto.TYPENAME, JsonAPITestHelper.toAttributeMap(seqBatchDto))));

    MolecularAnalysisRunItemDto runItemDto = MolecularAnalysisRunItemTestFixture.newMolecularAnalysisRunItem();
    String runItemDtoUuid = JsonAPITestHelper.extractId(
      sendPost(MolecularAnalysisRunItemDto.TYPENAME,
        JsonAPITestHelper.toJsonAPIMap(MolecularAnalysisRunItemDto.TYPENAME, JsonAPITestHelper.toAttributeMap(runItemDto))));

    SeqReactionDto seqReactionDto = SeqReactionTestFixture.newSeqReaction();

    OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "SeqReaction",
            sendPost(SeqReactionDto.TYPENAME, JsonAPITestHelper.toJsonAPIMap(SeqReactionDto.TYPENAME, JsonAPITestHelper.toAttributeMap(seqReactionDto),
                    JsonAPITestHelper.toRelationshipMap(
                            List.of(JsonAPIRelationship.of("seqPrimer", PcrPrimerDto.TYPENAME, primerUuid),
                                    JsonAPIRelationship.of("pcrBatchItem", PcrBatchItemDto.TYPENAME, pcrBatchItemDtoUuid),
                                    JsonAPIRelationship.of("molecularAnalysisRunItem", MolecularAnalysisRunItemDto.TYPENAME, runItemDtoUuid),
                                    JsonAPIRelationship.of("storageUnitUsage", "storage-unit-usage", UUID.randomUUID().toString()))),
                    null)
            ).extract().asString(),
            ValidationRestrictionOptions.builder().build());
  }
}
