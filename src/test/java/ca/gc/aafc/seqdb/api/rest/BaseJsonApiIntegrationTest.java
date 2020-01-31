package ca.gc.aafc.seqdb.api.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;

import ca.gc.aafc.seqdb.api.BaseHttpIntegrationTest;
import ca.gc.aafc.seqdb.api.repository.JsonSchemaAssertions;
import ca.gc.aafc.seqdb.api.security.ImportSampleAccounts;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

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
@Slf4j
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
  public static final String SCHEMA_BASE_PATH = "/json-schema";
  
  private static final String JSON_SCHEMA_FOLDER = "static/json-schema";


	@BeforeEach
	public final void before() {

		RestAssured.port = testPort;
		RestAssured.baseURI = IT_BASE_URI.toString();
		RestAssured.basePath = API_BASE_PATH;
		
		//set basic auth with ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME);
		authScheme.setPassword(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME);
		RestAssured.authentication = authScheme;
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
	 * Load a JSON Schema as String.
	 * @param filename file name of the JSON Schema under {@link BaseJsonApiIntegrationTest#JSON_SCHEMA_FOLDER}.
	 * @return
	 * @throws IOException
	 */
	private static String loadJsonSchemaAsString(String filename) throws IOException  {
		Resource  r = new ClassPathResource(JSON_SCHEMA_FOLDER + "/" + filename);
		try (Reader reader = new InputStreamReader(r.getInputStream(), StandardCharsets.UTF_8)){
		  return CharStreams.toString(reader);
		}
	}
	
  /**
   * Load Json Schema via network remote url
   * 
   * @param schemaFileName
   *          schemaFileName of the JSON Schema at location IT_BASE_URI + SCEHMA_BASE_PATH
   * @param responseJson
   *          The response json from service
   * @throws IOException
   * @throws URISyntaxException
   */
  protected void validateJsonSchemaByURL(String schemaFileName, String responseJson)
      throws IOException, URISyntaxException {

    URIBuilder uriBuilder = new URIBuilder(IT_BASE_URI);
    uriBuilder.setPort(testPort);
    uriBuilder.setPath(SCHEMA_BASE_PATH + "/" + schemaFileName);
    
    log.info("Validating {} schema against the following response: {}", schemaFileName, responseJson);

    JsonSchemaAssertions.assertJsonSchema(uriBuilder.build(), new StringReader(responseJson));
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
      Map<String, Object> attributeMap, Map<String, Object> relationshipMap, Integer id) {
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
    given().when().get(getResourceUnderTest() + "/0").then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }
  
  @Test
  public void resourceUnderTest_whenIdExists_returnOkAndBody()
      throws IOException, URISyntaxException {
    int id = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
    
    // Test with the crnk-compact header.
    ValidatableResponse responseCompact = given().header("crnk-compact", "true").when()
        .get(getResourceUnderTest() + "/" + id).then().statusCode(HttpStatus.OK.value());
    
    validateJsonSchemaByURL(getGetOneSchemaFilename(), responseCompact.extract().body().asString());
    
    // Test without the crnk-compact header.
    ValidatableResponse response = given().when().get(getResourceUnderTest() + "/" + id).then()
        .statusCode(HttpStatus.OK.value());
    
    validateJsonSchemaByURL(getGetOneSchemaFilename(), response.extract().body().asString());

    // cleanup
    sendDelete(id);
  }

  @Test
  public void resourceUnderTest_whenMultipleResources_returnOkAndBody()
      throws IOException, URISyntaxException {
    int id1 = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
    int id2 = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));

    // Test with the crnk-compact header.
    ValidatableResponse responseCompact = given().header("crnk-compact", "true").when()
        .get(getResourceUnderTest()).then().statusCode(HttpStatus.OK.value());

    validateJsonSchemaByURL(getGetManySchemaFilename(), responseCompact.extract().body().asString());
    
    // Test without the crnk-compact header.
    ValidatableResponse response = given().when().get(getResourceUnderTest()).then()
        .statusCode(HttpStatus.OK.value());
    
    validateJsonSchemaByURL(getGetManySchemaFilename(), response.extract().body().asString());

    // cleanup
    sendDelete(id1);
    sendDelete(id2);
  }

  @Test
  public void resourceUnderTest_whenDeleteExisting_returnNoContent() {
    int id = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
    sendDelete(id);
  }
  
  @Test
  public void resourceUnderTest_whenUpdating_returnOkAndResourceIsUpdated() {
    // Setup: create an resource

    int id = sendPost(toJsonAPIMap(buildCreateAttributeMap(), buildRelationshipMap()));
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
  protected ValidatableResponse sendGet(int id) {
    return given().header("crnk-compact", "true").when().get(getResourceUnderTest() + "/" + id).then()
        .statusCode(HttpStatus.OK.value());
  }

  /**
   * Sends a DELETE to the resource under test for the provided id. Asserts that it returns HTTP NO
   * CONTENT 204.
   * 
   * @param id
   */
  protected void sendDelete(int id) {
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
  protected int sendPost(Map<String, Object> dataMap) {
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
  protected int sendPost(String resourceName, Map<String, Object> dataMap) {
    Response response = given().header("crnk-compact", "true").contentType(JSON_API_CONTENT_TYPE).body(dataMap).when()
        .post(resourceName);
    response.then().statusCode(HttpStatus.CREATED.value());
    String id = (String) response.body().jsonPath().get("data.id");
    int idInt = Integer.parseInt(id);
    return idInt;
  }

  /**
   * Sends a PATCH to the resource under test for the provided id. Asserts that it returns HTTP OK
   * 200 and returns the response as {@link ValidatableResponse}.
   * 
   * @param dataMap body of the PATCH as Map
   * @return
   */
  protected ValidatableResponse sendPatch(int id, Map<String, Object> dataMap) {
    Response response = given().header("crnk-compact", "true").contentType(JSON_API_CONTENT_TYPE).body(dataMap).when()
        .patch(getResourceUnderTest() + "/" + id);
    return response.then().statusCode(HttpStatus.OK.value());
  }

}
