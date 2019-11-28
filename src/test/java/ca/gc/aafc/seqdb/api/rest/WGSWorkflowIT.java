package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.sql.DataSource;

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

public class WGSWorkflowIT extends BaseHttpIntegrationTest {

  public static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
  public static final String JSON_API_PATCH_CONTENT_TYPE = "application/json-patch+json";
  
  @Inject
  private DataSource datasource;
  
  @BeforeEach
  public final void before() {
    // Since H2 does not support creating enum types, the integration tests will only work using
    // postgresql. If you are using another dbms it will ignore all of the tests.
    // Assume that the tests are running only on postgresql, if not the tests will
    // be ignored.
    try {
      assumeTrue(
          "PostgreSQL".equals(datasource.getConnection().getMetaData().getDatabaseProductName()),
          "PreLibraryPrep tests were ignored since the test environment is not running on PostgreSQL.");
    } catch (SQLException e) {
      fail("Datasource could not be found. Make sure your connection is setup properly.");
    }
      
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
    Integer containerTypeId = createContainerType();
    Integer indexSetId = createIndexSet();
    List<Integer> ngsIndexIds = createNgsIndexes(indexSetId);
    List<Integer> sampleIds = createSamples();
    Integer chainId = createChain();
    List<Integer> sampleSrs = createSampleSelectionStepResources(chainId, sampleIds);
    List<Integer> plpSrIds = createPreLibraryPrepStepResources(chainId, sampleIds);
    Integer lpbId = createLibraryPrepBatch(chainId, containerTypeId, indexSetId);
    Integer lpbSrId = createLibraryPrepBatchStepResource(chainId, lpbId);
    List<Integer> libraryPrepIds = createLibraryPreps(lpbId, ngsIndexIds, sampleIds);
    
    // Step backwards through the workflow and delete everything:
    for (Integer id : libraryPrepIds) {
      delete("libraryPrep/" + id);
    }
    delete("stepResource/" + lpbSrId);
    delete("libraryPrepBatch/" + lpbId);
    for (Integer id : plpSrIds) {
      delete("stepResource/" + id);
    }
    for (Integer id : sampleSrs) {
      delete("stepResource/" + id);
    }
    delete("chain/" + chainId);
    for (Integer id : sampleIds) {
      delete("sample/" + id);
    }
    for (Integer id : ngsIndexIds) {
      delete("ngsIndex/" + id);
    }
    delete("indexSet/" + indexSetId);
    delete("containerType/" + containerTypeId);
  }
  
  private Integer createContainerType() {
    Integer ctId = doOperations(
      Collections.singletonList(
          new ImmutableMap.Builder<>()
              .put("op", "POST")
              .put("path", "containerType")
              .put("value", new ImmutableMap.Builder<>()
                  .put("attributes", new ImmutableMap.Builder<>()
                      .put("name", "test ct")
                      .put("baseType", "A")
                      .put("numberOfColumns", 12)
                      .put("numberOfRows", 8)
                      .build())
                  .put("id", "-100")
                  .put("type", "containerType")
                  .build())
              .build()
          )
      ).get(0);
    
    return ctId;
  }
  
  private Integer createIndexSet() {
    Integer isId = doOperations(
        Collections.singletonList(
            new ImmutableMap.Builder<>()
                .put("op", "POST")
                .put("path", "indexSet")
                .put("value", new ImmutableMap.Builder<>()
                    .put("attributes", new ImmutableMap.Builder<>()
                        .put("name", "test index set")
                        .build())
                    .put("id", "-100")
                    .put("type", "indexSet")
                    .build())
                .build()
            )
        ).get(0);
      
      return isId;
  }
  
  private List<Integer> createNgsIndexes(int indexSetId) {
    List<Integer> numbers = Lists.newArrayList(1, 2);
    
    List<Object> ops = numbers.stream().map(number -> 
        (Object) new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "ngsIndex")
            .put("value", new ImmutableMap.Builder<>()
                .put("id", number)
                .put("type", "ngsIndex")
                .put("attributes", new ImmutableMap.Builder<>()
                    .put("name", "index " + number)
                    .build())
                .put("relationships", new ImmutableMap.Builder<>()
                    .put("indexSet", relationship("indexSet", indexSetId))
                    .build())
                .build())
            .build()
        ).collect(Collectors.toList());
        
    List<Integer> ngsIndexIds = doOperations(ops);
    
    return ngsIndexIds;
  }
  
  private List<Integer> createSamples() {
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
  
  private Integer createChain() {
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
  
  private List<Integer> createSampleSelectionStepResources(int chainId, List<Integer> sampleIds) {
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
    
    List<Integer> ids = doOperations(selectSamplesOps);
    return ids;
  }
  
  private List<Integer> createPreLibraryPrepStepResources(int chainId, List<Integer> sampleIds) {
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
                    .put("value", "SHEARING")
                    .build())
                .put("relationships", new ImmutableMap.Builder<>()
                    .put("chain", relationship("chain", chainId))
                    .put("chainStepTemplate", relationship("chainStepTemplate", plpChainStepTemplateId))
                    .put("sample", relationship("sample", sampleId))
                    .put("preLibraryPrep", relationship("preLibraryPrep", plpIds.get(sampleIds.indexOf(sampleId))))
                    .build())
                .build())
            .build()
        ).collect(Collectors.toList());
    
    List<Integer> ids = doOperations(plpStepResourceOps);
    
    return ids;
  }
  
  private Integer createLibraryPrepBatch(int chainId, int containerTypeId, int indexSetId) {
    Integer lpbId = doOperations(
        Collections.singletonList(
            new ImmutableMap.Builder<>()
                .put("op", "POST")
                .put("path", "libraryPrepBatch")
                .put("value", new ImmutableMap.Builder<>()
                    .put("id", "-100")
                    .put("type", "libraryPrepBatch")
                    .put("attributes", new ImmutableMap.Builder<>()
                        .put("name", "test batch")
                        .build())
                    .put("relationships", new ImmutableMap.Builder<>()
                        .put("containerType", relationship("containerType", containerTypeId))
                        .put("indexSet", relationship("indexSet", indexSetId))
                        .build())
                    .build())
                .build()
            )
        ).get(0);
        
    return lpbId;  
  }
  
  private Integer createLibraryPrepBatchStepResource(int chainId, int libraryPrepBatchId) {
    int libraryPrepBatchChainStepTemplateId = getRequest("/chainStepTemplate?filter[stepTemplate.name]=Library Prep")
        .jsonPath()
        .getInt("data.id[0]");
    
    Integer srId = doOperations(
        Collections.singletonList(
            new ImmutableMap.Builder<>()
                .put("op", "POST")
                .put("path", "stepResource")
                .put("value", new ImmutableMap.Builder<>()
                    .put("id", "-100")
                    .put("type", "stepResource")
                    .put("attributes", new ImmutableMap.Builder<>()
                        .put("type", "INPUT")
                        .put("value", "LIBRARY_PREP_BATCH")
                        .build())
                    .put("relationships", new ImmutableMap.Builder<>()
                        .put("chain", relationship("chain", chainId))
                        .put("chainStepTemplate", relationship("chainStepTemplate", libraryPrepBatchChainStepTemplateId))
                        .put("libraryPrepBatch", relationship("libraryPrepBatch", libraryPrepBatchId))
                        .build())
                    .build())
                .build()
            )
        ).get(0);
    
    return srId;
  }
  
  private List<Integer> createLibraryPreps(int libraryPrepbatchId, List<Integer> ngsIndexIds, List<Integer> sampleIds) {
    List<Object> libraryPrepOps = Lists.newArrayList(
        new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "libraryPrep")
            .put("value", new ImmutableMap.Builder<>()
                .put("id", ngsIndexIds.get(0).toString())
                .put("type", "libraryPrep")
                .put("attributes", new ImmutableMap.Builder<>()
                    .put("wellRow", "A")
                    .put("wellColumn", 1)
                    .put("quality", "good")
                    .build())
                .put("relationships", new ImmutableMap.Builder<>()
                    .put("libraryPrepBatch", relationship("libraryPrepBatch", libraryPrepbatchId))
                    .put("sample", relationship("sample", sampleIds.get(0)))
                    .put("indexI5", relationship("ngsIndex", ngsIndexIds.get(0)))
                    .build())
                .build())
            .build(),
        new ImmutableMap.Builder<>()
            .put("op", "POST")
            .put("path", "libraryPrep")
            .put("value", new ImmutableMap.Builder<>()
                .put("id", ngsIndexIds.get(1).toString())
                .put("type", "libraryPrep")
                .put("attributes", new ImmutableMap.Builder<>()
                    .put("wellRow", "A")
                    .put("wellColumn", 2)
                    .put("quality", "very good")
                    .build())
                .put("relationships", new ImmutableMap.Builder<>()
                    .put("libraryPrepBatch", relationship("libraryPrepBatch", libraryPrepbatchId))
                    .put("sample", relationship("sample", sampleIds.get(1)))
                    .put("indexI5", relationship("ngsIndex", ngsIndexIds.get(1)))
                    .build())
                .build())
            .build()
        );
    
    List<Integer> libraryPrepIds = doOperations(libraryPrepOps);
    return libraryPrepIds;
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
  
  private void delete(String endpoint) {
    Response response = given()
        .header("Crnk-Compact", "true")
        .accept(JSON_API_CONTENT_TYPE)
        .contentType(JSON_API_CONTENT_TYPE)
        .delete(endpoint);
    
    int status = response
        .statusCode();
    
    if (!Integer.toString(status).startsWith("2")) {
      throw new RuntimeException(response.body().print());
    }
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
