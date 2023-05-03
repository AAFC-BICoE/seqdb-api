package ca.gc.aafc.seqdb.api.openapi;

import ca.gc.aafc.dina.testsupport.BaseRestAssuredTest;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPIRelationship;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.dina.testsupport.specs.OpenAPI3Assertions;
import ca.gc.aafc.dina.testsupport.specs.ValidationRestrictionOptions;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.*;
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
public class SeqSubmissionOpenApiIT extends BaseRestAssuredTest{
    protected SeqSubmissionOpenApiIT(){super("/api");}
    @SneakyThrows
    @Test
    void seqSubmission_SpecValid() {

        SeqBatchDto seqBatchDto = SeqBatchTestFixture.newSeqBatch();
        seqBatchDto.setExperimenters(null);
        String seqBatchDtoUuid = JsonAPITestHelper.extractId(
                sendPost(SeqBatchDto.TYPENAME,
                        JsonAPITestHelper.toJsonAPIMap(SeqBatchDto.TYPENAME, JsonAPITestHelper.toAttributeMap(seqBatchDto))));


        SeqSubmissionDto seqSubmissionDto = SeqSubmissionTestFixture.newSeqSubmission();
        seqSubmissionDto.setSubmittedBy(null);

        OpenAPI3Assertions.assertRemoteSchema(OpenAPIConstants.SEQDB_API_SPECS_URL, "SeqSubmission",
                sendPost(SeqSubmissionDto.TYPENAME, JsonAPITestHelper.toJsonAPIMap(SeqSubmissionDto.TYPENAME, JsonAPITestHelper.toAttributeMap(seqSubmissionDto),
                        JsonAPITestHelper.toRelationshipMap(
                                List.of(JsonAPIRelationship.of("seqBatch", SeqBatchDto.TYPENAME, seqBatchDtoUuid),
                                        JsonAPIRelationship.of("submittedBy", "person", UUID.randomUUID().toString())
                                )),
                        null)
                ).extract().asString(),
                ValidationRestrictionOptions.builder().build());
    }
}
