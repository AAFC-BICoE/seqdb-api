package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.CharStreams;

import ca.gc.aafc.seqdb.api.BaseHttpIntegrationTest;
import ca.gc.aafc.seqdb.api.security.ImportSampleAccounts;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
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
@TestPropertySource(properties="import-sample-accounts=true")
public abstract class BaseJsonApiIntegrationTest extends BaseHttpIntegrationTest {
  
  public static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
  public static final String IT_BASE_URI = "http://localhost";
  public static final String API_BASE_PATH = "/api";
  
  private static final String JSON_SCHEMA_FOLDER = "static/json-schema";

	@Before
	public final void before() {
		RestAssured.port = testPort;
		RestAssured.baseURI = IT_BASE_URI;
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
   * Creates a JSON API Map from the provided attributes. "type" will be equal to
   * {@link BaseJsonApiIntegrationTest#getResourceUnderTest()}.
   * 
   * @param attributeMap
   * @return
   */
  protected Map<String, Object> toJsonAPIMap(Map<String, Object> attributeMap) {
    return toJsonAPIMap(getResourceUnderTest(), attributeMap, null);
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
  protected static Map<String, Object> toJsonAPIMap(String typeName,
      Map<String, Object> attributeMap, Integer id) {
    ImmutableMap.Builder<String, Object> bldr = new ImmutableMap.Builder<>();
    bldr.put("type", typeName);
    if (id != null) {
      bldr.put("id", id);
    }
    bldr.put("attributes", attributeMap);
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
  public void resourceUnderTest_whenIdExists_returnOkAndBody() {
    int id = sendPost(toJsonAPIMap(buildCreateAttributeMap()));
    ValidatableResponse response = given().when()
        .get(getResourceUnderTest() + "/" + id)
        .then().statusCode(HttpStatus.OK.value());
    
    //cleanup
    sendDelete(id);
    
    //return response;
    //there is some issue with the schema includes reference to be parsed by Rest Assured API
//    response.assertThat().body(matchesJsonSchema(jsonApiSchema));
  }

  
	@Test
  public void resourceUnderTest_whenMultipleResources_returnOkAndBody() throws IOException {
	  int id1 = sendPost(toJsonAPIMap(buildCreateAttributeMap()));
	  int id2 = sendPost(toJsonAPIMap(buildCreateAttributeMap()));
	  
    ValidatableResponse response = given().when()
        .get(getResourceUnderTest())
        .then().statusCode(HttpStatus.OK.value());
    String jsonSchema = loadJsonSchemaAsString(getGetManySchemaFilename());
    response.assertThat().body(matchesJsonSchema(jsonSchema));
    
    //cleanup
    sendDelete(id1);
    sendDelete(id2);
  }

  @Test
  public void resourceUnderTest_whenDeleteExisting_returnNoContent() {
    int id = sendPost(toJsonAPIMap(buildCreateAttributeMap()));
    sendDelete(id);
  }
  
  @Test
  public void resourceUnderTest_whenUpdating_returnOkAndResourceIsUpdated() {
    // Setup: create an resource
    int id = sendPost(toJsonAPIMap(buildCreateAttributeMap()));
    
    Map<String, Object> updatedAttributeMap = buildUpdateAttributeMap();

    // update the resource
    sendPatch(id, toJsonAPIMap(getResourceUnderTest(), updatedAttributeMap, id));

    ValidatableResponse responseUpdate = sendGet(id);

    // verify
    
    for(String attributeKey: updatedAttributeMap.keySet()) {
      responseUpdate.body("data.attributes." + attributeKey, equalTo(updatedAttributeMap.get(attributeKey)));
    }
    
    //cleanup
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
    return given().when().get(getResourceUnderTest() + "/" + id).then()
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
   * Sends a POST to the resource under test for the provided id. Asserts that it returns HTTP
   * CREATED 201. Extracts the newly assigned id and returns it.
   * 
   * @param dataMap body of the POST as Map
   * @return id of the newly created resource
   */
  protected int sendPost(Map<String, Object> dataMap) {
    Response response = given().contentType(JSON_API_CONTENT_TYPE).body(dataMap).when()
        .post(getResourceUnderTest());
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
    Response response = given().contentType(JSON_API_CONTENT_TYPE).body(dataMap).when()
        .patch(getResourceUnderTest() + "/" + id);
    return response.then().statusCode(HttpStatus.OK.value());
  }

}
