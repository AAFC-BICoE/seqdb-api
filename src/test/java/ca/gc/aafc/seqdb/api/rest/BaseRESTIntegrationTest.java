package ca.gc.aafc.seqdb.api.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

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
	
	private static final int HTTP_OK = 200;
	private static final int HTTP_NOT_FOUND = 404;
	private static final int HTTP_NO_CONTENT = 204;
	
	
	@Value("${local.server.port}")
	protected int port;

	protected String jsonApiSchema;

	@Before
	public final void before() {
		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";
		RestAssured.basePath = "/api";
		
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
				.statusCode(HTTP_OK);
		return response;
		//there is some issue with the schema includes reference to be parsed by Rest Assured API
//		response.assertThat().body(matchesJsonSchema(jsonApiSchema));
	}


	protected void testFindOne_NotFound(String path) {
		given().when().get(path).then().statusCode(HTTP_NOT_FOUND);
	}

	protected void testFindMany(String path) {
		ValidatableResponse response = given().when().get(path).then()
				.statusCode(HTTP_OK);

		String string = response.extract().asString();
		System.out.println(string);
		response.assertThat().body(matchesJsonSchema(jsonApiSchema));
	}

	protected void testDelete(String path) {

		given().contentType("application/json").when().delete(path).then().statusCode(HTTP_NO_CONTENT);
	}

	protected int testCreate(String path, Map dataMap) {

		Response response = given().contentType("application/vnd.api+json")
				.body(dataMap).when().post(path);
	    response.then().statusCode(201); //status code Created.
		response.prettyPrint();
		String id = (String) response.body().jsonPath().get("data.id");
		int idInt = Integer.parseInt(id);
		return idInt;
	}

	protected ValidatableResponse testUpdate(String path, Map dataMap) {

		Response response = given().contentType("application/vnd.api+json")
				.body(dataMap).when().patch(path);
		response.prettyPrint();
		return response.then().statusCode(HTTP_OK); //status code Created.

	}

}
