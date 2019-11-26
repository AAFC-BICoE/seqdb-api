package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import ca.gc.aafc.seqdb.api.BaseHttpIntegrationTest;
import ca.gc.aafc.seqdb.api.security.ImportSampleAccounts;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;

public class WGSWorkflowIntegrationTest extends BaseHttpIntegrationTest {

  public static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
  public static final String JSON_API_PATCH_CONTENT_TYPE = "application/json-patch+json";
  
  @BeforeEach
  public final void before() {
    RestAssured.port = testPort;
    RestAssured.baseURI = BaseJsonApiIntegrationTest.IT_BASE_URI.toString();
    RestAssured.basePath = BaseJsonApiIntegrationTest.API_BASE_PATH;
    
    //set basic auth with ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME);
    authScheme.setPassword(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME);
    RestAssured.authentication = authScheme;
  }
  
  @Test
  public void testWGSWorkflow() {
    List<Integer> sampleIds = createTestSamples();
    Integer chainId = createTestChain();
    createSampleSelectionStepResources(chainId, sampleIds);
    createPreLibraryPrepStepResources(chainId, sampleIds);
  }
  
  private List<Integer> createTestSamples() {
    // Add test samples:
    ImmutableMap<Object, Object> sample1 = new ImmutableMap.Builder<>()
        .put("attributes", new ImmutableMap.Builder<>()
            .put("name", "SAMP1")
            .put("version", "A")
            .build())
        .put("id", "100")
        .put("type", "sample")
        .build();
    ImmutableMap<Object, Object> sample2 = new ImmutableMap.Builder<>()
        .put("attributes", new ImmutableMap.Builder<>()
            .put("name", "SAMP2")
            .put("version", "A")
            .build())
        .put("id", "101")
        .put("type", "sample")
        .build();
    
    List<Object> sampleOperations = Lists.newArrayList(sample1, sample2).stream()
        .map(sample -> new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "sample")
            .put("value", sample)
            .build()).collect(Collectors.toList());
    
    return doOperations(sampleOperations);
  }
  
  private Integer createTestChain() {
    int wgsTemplateId = getRequest("/chainTemplate")
        .jsonPath()
        .getInt("data.find { it.attributes.name == \"Whole Genome Sequencing\" }.id");
    
    int userGroupId = getRequest("/group")
        .jsonPath()
        .getInt("data.find { it.attributes.groupName == \"User Group\" }.id");
    
    List<Object> addChainOps = Collections.singletonList(
        new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "chain")
            .put("value", new ImmutableMap.Builder<>()
                .put("id", "101")
                .put("type", "chain")
                .put("attributes", new ImmutableMap.Builder<>()
                    .put("name", "test chain")
                    .put("dateCreated", "2019-11-26")
                    .build())
                .put("relationships", new ImmutableMap.Builder<>()
                    .put("chainTemplate", relationship("chainTemplate", wgsTemplateId))
                    .put("group", relationship("group", userGroupId))
                    .build())
                .build())
            .build()
    );
    
    return doOperations(addChainOps).get(0);
  }
  
  private void createSampleSelectionStepResources(int chainId, List<Integer> sampleIds) {
    int selectionChainStepTemplateId = getRequest("/chainStepTemplate?filter[stepTemplate.name]=Select Samples")
        .jsonPath()
        .getInt("data.id[0]");
    
    List<Object> selectSamplesOps = sampleIds.stream().map(sampleId -> 
        new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "stepResource")
            .put("value", new ImmutableMap.Builder<>()
                .put("id", sampleId.toString())
                .put("type", "stepResource")
                .put("attributes", new ImmutableMap.Builder<>()
                    .put("type", "INPUT")
                    .put("value", "SAMPLE")
                    .build())
                .put("relationships", new ImmutableMap.Builder<>()
                    .put("chain", relationship("chain", chainId))
                    .put("sample", relationship("sample", sampleId))
                    .put("chainStepTemplate", relationship("chainStepTemplate", selectionChainStepTemplateId))
                    .build())
                .build())
            .build()
        ).collect(Collectors.toList());
    
    doOperations(selectSamplesOps);
  }
  
  private void createPreLibraryPrepStepResources(int chainId, List<Integer> sampleIds) {
    int plpChainStepTemplateId = getRequest("/chainStepTemplate?filter[stepTemplate.name]=Pre Library Prep")
        .jsonPath()
        .getInt("data.id[0]");
    
    List<Object> plpOps = sampleIds.stream().map(sampleId -> 
        new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "preLibraryPrep")
            .put("value", new ImmutableMap.Builder<>()
                .put("id", sampleId.toString())
                .put("type", "preLibraryPrep")
                .put("attributes", new ImmutableMap.Builder<>()
                    .put("preLibraryPrepType", "SHEARING")
                    .put("inputAmount", 123)
                    .put("notes", "test notes")
                    .build())
                .build())
            .build()
        ).collect(Collectors.toList());
    
    List<Integer> plpIds = doOperations(plpOps);
    
    List<Object> plpStepResourceOps = sampleIds.stream().map(sampleId -> 
        new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "stepResource")
            .put("value", new ImmutableMap.Builder<>()
                .put("id", sampleId.toString())
                .put("type", "stepResource")
                .put("attributes", new ImmutableMap.Builder<>()
                    .put("type", "INPUT")
                    .put("value", "PRE_LIBRARY")
                    .build())
                .put("relationships", new ImmutableMap.Builder<>()
                    .put("sample", relationship("sample", sampleId))
                    .put("preLibraryPrep", relationship("preLibraryPrep", plpIds.get(sampleIds.indexOf(sampleId))))
                    .build())
                .build())
            .build()
        ).collect(Collectors.toList());
    
    doOperations(plpStepResourceOps);
  }
  
  private Object relationship(String type, int id) {
    return new ImmutableMap.Builder<>()
        .put("data", new ImmutableMap.Builder<>()
            .put("type", type)
            .put("id", Integer.toString(id))
            .build())
        .build();
  }
  
  private ResponseBody getRequest(String endpoint) {
    return given()
        .header("Crnk-Compact", "true")
        .accept(JSON_API_CONTENT_TYPE)
        .contentType(JSON_API_CONTENT_TYPE)
        .get(endpoint)
        .body();
  }
  
  private List<Integer> doOperations(List<Object> operations) {
    Response response = given()
        .header("Crnk-Compact", "true")
        .accept(JSON_API_PATCH_CONTENT_TYPE)
        .contentType(JSON_API_PATCH_CONTENT_TYPE)
        .body(operations)
        .patch("/operations");
    
    if (response.body().jsonPath().getList("errors").get(0) != null) {
      throw new RuntimeException(response.body().print());
    }
    
    try {
      return response
          .jsonPath()
          .getList("data.id")
          .stream()
          .map(idString -> Integer.parseInt((String) idString))
          .collect(Collectors.toList());
    } catch(Exception e) {
      throw new RuntimeException(response.body().print(), e);
    }
  }

}
