package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import ca.gc.aafc.seqdb.api.BaseHttpIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

/**
 * 
 * Base for JSON API Integration tests based on RestAssured.
 * 
 * Provides:
 * - Running web server backed by an h2 database
 * - Authenticated user (as {@link ImportSampleAccounts#IMPORTED_USER_ACCOUNT_NAME})
 * - Validation of the response against JSON Schema
 * - Helper methods to build JSON API compliant Map ({@link BaseJsonApiIntegrationTest#toJsonAPIMap(String, Map)}
 *
 */
public abstract class BaseJsonApiIntegrationTest extends BaseHttpIntegrationTest {
  
  public static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
  
  private static final ObjectMapper IT_OBJECT_MAPPER = new ObjectMapper();
  private static final TypeReference<Map<String, Object>> IT_OM_TYPE_REF = new TypeReference<Map<String, Object>>() {};
   
  public static final URI IT_BASE_URI;
  static {
    URIBuilder uriBuilder = new URIBuilder();
    uriBuilder.setScheme("http");
    uriBuilder.setHost("localhost");
    URI tmpURI = null;
    try {
      tmpURI = uriBuilder.build();
    } catch (URISyntaxException e) {
      fail(e.getMessage());
    }
    IT_BASE_URI = tmpURI;
    IT_OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
  }

  public static final String API_BASE_PATH = "/api";


	@BeforeEach
	public final void before() {

		RestAssured.port = testPort;
		RestAssured.baseURI = IT_BASE_URI.toString();
		RestAssured.basePath = API_BASE_PATH;
	}
	
	 /**
   * Get the name of the resource under test without slash(es).
   * e.g. /api/region/1 -> resource = "region"
   * @return
   */
  protected abstract String getResourceUnderTest();
  
  /**
   * Returns the JSON Schema for get-one of the resource under test.
   * @return
   */
	protected abstract String getGetOneSchemaFilename();
	
  /**
   * Returns the JSON Schema for get-many of the resource under test.
   * @return
   */
	protected abstract String getGetManySchemaFilename();
	
	/**
	 * Creates an attributes map to create a new entity.
	 * If a field is unique it's to responsibility of the implementation to return a different one on each call.
	 * @return
	 */
	protected abstract Map<String, Object> buildCreateAttributeMap();
	
	protected abstract Map<String, Object> buildUpdateAttributeMap();
	
  /**
   * Override if a relationship map is required.
   * 
   * @return relationship map or null if none
   */
  protected Map<String, Object> buildRelationshipMap() {
    return null;
  }

  /**
   * Create an attribute map for the provided object. Attributes with nulls will be skipped.
   * 
   * @param obj
   * @return attribute map for the provided object
   */
  protected Map<String, Object> toAttributeMap(Object obj) {
    return IT_OBJECT_MAPPER.convertValue(obj, IT_OM_TYPE_REF);
  }
  
  /**
   * Creates a JSON API Map from the provided attributes. "type" will be equal to
   * {@link BaseJsonApiIntegrationTest#getResourceUnderTest()}.
   * 
   * @param attributeMap
   * @return
   */
  protected Map<String, Object> toJsonAPIMap(Map<String, Object> attributeMap, Map<String, Object> relationshipMap) {
    return toJsonAPIMap(getResourceUnderTest(), attributeMap, relationshipMap, null);
  }

  /**
   * Creates a JSON API Map from the provided type name, attributes and id.
   * 
   * @param typeName
   *          "type" in JSON API
   * @param attributeMap
   *          key/value representing the "attributes" in JSON API
   * @param id
   *          id of the resource or null if there is none
   * @return
   */
  public static Map<String, Object> toJsonAPIMap(String typeName,
      Map<String, Object> attributeMap, Map<String, Object> relationshipMap, String id) {
    ImmutableMap.Builder<String, Object> bldr = new ImmutableMap.Builder<>();
    bldr.put("type", typeName);
    if (id != null) {
      bldr.put("id", id);
    }
    bldr.put("attributes", attributeMap);
    if(relationshipMap != null) {
      bldr.put("relationships", relationshipMap);
    }
    return ImmutableMap.of("data", bldr.build());
  }

  /**
   * Try to get a non-existing id (id = 0).
   * 
   */
  @Test
  public void resourceUnderTest_whenIdDoesntExist_returnNotFound() {
    given().when().get(getResourceUnderTest() + "/6a2f41a3-c54c-fce8-32d2-0324e1c32e22").then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
  
  @Test
  public void resourceUnderTest_whenIdExists_returnOkAndBody()
      throws IOException, URISyntaxException {
    String id = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
    
    // Test with the crnk-compact header.
    given().header("crnk-compact", "true").when()
        .get(getResourceUnderTest() + "/" + id).then().statusCode(HttpStatus.OK.value());
    
    // Test without the crnk-compact header.
    given().when().get(getResourceUnderTest() + "/" + id).then()
        .statusCode(HttpStatus.OK.value());

    // cleanup
    sendDelete(id);
  }

  @Test
  public void resourceUnderTest_whenMultipleResources_returnOkAndBody()
      throws IOException, URISyntaxException {
    String id1 = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
    String id2 = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));

    // Test with the crnk-compact header.
    given().header("crnk-compact", "true").when()
        .get(getResourceUnderTest()).then().statusCode(HttpStatus.OK.value());

    // Test without the crnk-compact header.
    given().when().get(getResourceUnderTest()).then()
        .statusCode(HttpStatus.OK.value());
    
    // cleanup
    sendDelete(id1);
    sendDelete(id2);
  }

  @Test
  public void resourceUnderTest_whenDeleteExisting_returnNoContent() {
    String id = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
    sendDelete(id);
  }
  
  @Test
  public void resourceUnderTest_whenUpdating_returnOkAndResourceIsUpdated() {
    // Setup: create an resource

    String id = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
    Map<String, Object> updatedAttributeMap = buildUpdateAttributeMap();

    // update the resource
    sendPatch(id, toJsonAPIMap(getResourceUnderTest(), updatedAttributeMap, buildRelationshipMap(), id));

    ValidatableResponse responseUpdate = sendGet(id);

    // verify
    for (String attributeKey : updatedAttributeMap.keySet()) {
      responseUpdate.body("data.attributes." + attributeKey,
          equalTo(updatedAttributeMap.get(attributeKey)));
    }

    // cleanup
    sendDelete(id);
  }

  /**
   * Sends a GET to the resource under test for the provided id. Asserts that it returns HTTP OK 200 and
   * returns the response as {@link ValidatableResponse}
   * 
   * @param id id of the resource to get
   * @return
   */
  protected ValidatableResponse sendGet(String id) {
    return given().header("crnk-compact", "true").when().get(getResourceUnderTest() + "/" + id).then()
        .statusCode(HttpStatus.OK.value());
  }

  /**
   * Sends a DELETE to the resource under test for the provided id. Asserts that it returns HTTP NO
   * CONTENT 204.
   * 
   * @param id
   */
  protected void sendDelete(String id) {
    given().contentType(JSON_API_CONTENT_TYPE).when().delete(getResourceUnderTest() + "/" + id)
        .then().statusCode(HttpStatus.NO_CONTENT.value());
  }

  /**
   * Sends a POST to the resource under test. Asserts that it returns HTTP
   * CREATED 201. Extracts the newly assigned id and returns it.
   * 
   * @param dataMap body of the POST as Map
   * @return id of the newly created resource
   */
  protected String sendPost(Map<String, Object> dataMap) {
    return sendPost(getResourceUnderTest(), dataMap);
  }
  
  /**
   * Sends a POST to the resourceName. Asserts that it returns HTTP
   * CREATED 201. Extracts the newly assigned id and returns it.
   * 
   * @resourceName name of the resource to POST to
   * @param dataMap body of the POST as Map
   * @return id of the newly created resource
   */
  protected String sendPost(String resourceName, Map<String, Object> dataMap) {
    Response response = given().header("crnk-compact", "true").contentType(JSON_API_CONTENT_TYPE).body(dataMap).when()
        .post(resourceName);
    response.then().statusCode(HttpStatus.CREATED.value());
    String id = (String) response.body().jsonPath().get("data.id");
    return id;
  }

  /**
   * Sends a PATCH to the resource under test for the provided id. Asserts that it returns HTTP OK
   * 200 and returns the response as {@link ValidatableResponse}.
   * 
   * @param dataMap body of the PATCH as Map
   * @return
   */
  protected ValidatableResponse sendPatch(String id, Map<String, Object> dataMap) {
    Response response = given().header("crnk-compact", "true").contentType(JSON_API_CONTENT_TYPE).body(dataMap).when()
        .patch(getResourceUnderTest() + "/" + id);
    return response.then().statusCode(HttpStatus.OK.value());
  }

}
