package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.seqdb.api.BaseHttpIntegrationTest;
import ca.gc.aafc.seqdb.api.security.ImportSampleAccounts;
import io.crnk.core.engine.internal.utils.IOUtils;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@TestPropertySource(properties="import-sample-accounts=true")
public abstract class BaseRESTIntegrationTest extends BaseHttpIntegrationTest {
  
  public static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
  public static final String IT_BASE_URI = "http://localhost";
  public static final String API_BASE_PATH = "/api";

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
  
	protected abstract String getGetOneSchemaFilename();
	protected abstract String getGetManySchemaFilename();
	
	/**
	 * Creates an attributes map to create a new entity.
	 * @return
	 */
	protected abstract Map<String, Object> buildCreateAttributeMap();

	private static String loadFile(String filename) throws IOException  {
		InputStream inputStream = BaseRESTIntegrationTest.class.getClassLoader()
				.getResourceAsStream("json-schema/" + filename);
		byte[] input = IOUtils.readFully(inputStream);
		return new String(input);

	}
	
  protected static Map<String, Object> toJsonAPIMap(String typeName,
      Map<String, Object> attributeMap) {
    return toJsonAPIMap(typeName, attributeMap, null);
  }
  
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

	protected ValidatableResponse testFindOne(int id) {
		ValidatableResponse response = given().when()
		    .get(getResourceUnderTest() + "/" + id)
		    .then().statusCode(HttpStatus.OK.value());
		
		return response;
		//there is some issue with the schema includes reference to be parsed by Rest Assured API
//		response.assertThat().body(matchesJsonSchema(jsonApiSchema));
	}

  /**
   * Try to get a non-existing id (id =0).
   * 
   */
  @Test
  public void testFindOne_NotFound() {
    given().when().get(getResourceUnderTest() + "/0").then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

	@Test
  public void testFindMany() throws IOException {
    ValidatableResponse response = given().when()
        .get(getResourceUnderTest())
        .then().statusCode(HttpStatus.OK.value());
    String jsonSchema = loadFile(getGetManySchemaFilename());
    response.assertThat().body(matchesJsonSchema(jsonSchema));
  }
	
  @Test
  public void testFindOne_Found() {
    int id = sendPost(toJsonAPIMap(getResourceUnderTest(), buildCreateAttributeMap()));
    testFindOne(id);
  }

  @Test
  public void testCreateAndDelete() {
    int id = sendPost(toJsonAPIMap(getResourceUnderTest(), buildCreateAttributeMap()));
    sendDelete(id);
  }
  
  protected ValidatableResponse sendGet(int id) {
    return given().when()
        .get(getResourceUnderTest() + "/" + id)
        .then().statusCode(HttpStatus.OK.value());
  }

	protected void sendDelete(int id) {
		given().contentType(JSON_API_CONTENT_TYPE)
		.when().delete(getResourceUnderTest() + "/" + id)
		.then().statusCode(HttpStatus.NO_CONTENT.value());
	}

	/**
	 * Post data to the resource under test.
	 * Asserts that 201 Created is received
	 * 
	 * @return id of the newly created resource
	 */
	protected int sendPost(Map<String, Object> dataMap) {
		Response response = given().contentType(JSON_API_CONTENT_TYPE)
				.body(dataMap).when().post(getResourceUnderTest());
	    response.then().statusCode(HttpStatus.CREATED.value());
	    
		//response.prettyPrint();
		String id = (String) response.body().jsonPath().get("data.id");
		int idInt = Integer.parseInt(id);
		return idInt;
	}

	protected ValidatableResponse testUpdate(String path, Map<String, Object> dataMap) {

		Response response = given().contentType(JSON_API_CONTENT_TYPE)
				.body(dataMap).when().patch(path);
		return response.then().statusCode(HttpStatus.OK.value()); //status code Created.
	}

}
