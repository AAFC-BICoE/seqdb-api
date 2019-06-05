package ca.gc.aafc.seqdb.api.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.test.context.TestPropertySource;

import com.google.common.collect.ImmutableMap;

import org.springframework.http.HttpStatus;


import io.crnk.core.engine.internal.utils.IOUtils;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;


import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import ca.gc.aafc.seqdb.api.BaseHttpIntegrationTest;

@TestPropertySource(properties="import-sample-accounts=true")
public abstract class BaseRESTIntegrationTest extends BaseHttpIntegrationTest {
  
  public static final String JSON_API_CONTENT_TYPE = "application/vnd.api+json";
  public static final String IT_BASE_URI = "http://localhost";
  public static final String API_BASE_PATH = "/api";
	
	@Value("${local.server.port}")
	protected int port;

	protected String jsonApiSchema;

	@Before
	public final void before() {
		RestAssured.port = port;
		RestAssured.baseURI = IT_BASE_URI;
		RestAssured.basePath = API_BASE_PATH;
		
		//set basic auth with Admin/Admin
		PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
		authScheme.setUserName("User");
		authScheme.setPassword("User");
		RestAssured.authentication = authScheme;
	}

	protected void loadJsonApiSchema() {
		loadJsonApiSchema("json-api-schema.json");
	}

	protected void loadJsonApiSchema(String jsonFile) {
		try {
			jsonApiSchema = loadFile(jsonFile);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static String loadFile(String filename) throws IOException  {
		InputStream inputStream = BaseRESTIntegrationTest.class.getClassLoader()
				.getResourceAsStream(filename);
		byte[] input = IOUtils.readFully(inputStream);
		return new String(input);

	}

	protected ValidatableResponse testFindOne(String path) {
		ValidatableResponse response = given().when().get(path).then()
				.statusCode(HttpStatus.OK.value());
		return response;
		//there is some issue with the schema includes reference to be parsed by Rest Assured API
//		response.assertThat().body(matchesJsonSchema(jsonApiSchema));
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

	protected void testFindOne_NotFound(String path) {
		given().when().get(path).then().statusCode(HttpStatus.NOT_FOUND.value());
	}

	protected void testFindMany(String path) {
		ValidatableResponse response = given().when().get(path).then()
				.statusCode(HttpStatus.OK.value());

		String string = response.extract().asString();
		System.out.println(string);
		response.assertThat().body(matchesJsonSchema(jsonApiSchema));
	}

	protected void testDelete(String path) {
		given().contentType("application/json")
		.when().delete(path)
		.then().statusCode(HttpStatus.NO_CONTENT.value());
	}

	protected int testCreate(String path, Map<String, Object> dataMap) {

		Response response = given().contentType(JSON_API_CONTENT_TYPE)
				.body(dataMap).when().post(path);
	    response.then().statusCode(201); //status code Created.
		response.prettyPrint();
		String id = (String) response.body().jsonPath().get("data.id");
		int idInt = Integer.parseInt(id);
		return idInt;
	}

	protected ValidatableResponse testUpdate(String path, Map<String, Object> dataMap) {

		Response response = given().contentType(JSON_API_CONTENT_TYPE)
				.body(dataMap).when().patch(path);
		response.prettyPrint();
		return response.then().statusCode(HttpStatus.OK.value()); //status code Created.

	}

}
