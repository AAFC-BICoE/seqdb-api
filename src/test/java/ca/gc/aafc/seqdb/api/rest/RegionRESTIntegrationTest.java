package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;


public class RegionRESTIntegrationTest extends BaseRESTIntegrationTest {

	@Before
	public final void setup() {
		RestAssured.port = port;
		//load regionJSONSchema schema
		loadJsonApiSchema("json-schema/GETregionJSONSchema.json");
	}

	@Test
    public void testFindOne_NotFound() {
        testFindOne_NotFound("/region/0");
	}

	@Test
    public void testFindOne_Found() {
		loadJsonApiSchema("json-schema/regionJSONSchema.json");
		testFindOne("/region/1");
	}

	@Test
	public void testFindMany() {
		loadJsonApiSchema("json-schema/GETregionJSONSchema.json");
		testFindMany("/region");
	}

  @Test
  public void testCreateTask() {

    Map<String, Object> attributeMap = new ImmutableMap.Builder<String, Object>()
        .put("name", "test name")
        .put("description", "test description")
        .put("symbol", "test symbol").build();
    
    int id = testCreate("/region", toJsonAPIMap("region", attributeMap));
    testDelete("/region/" + id);
  }

  @Test
  public void testUpdateTask() {
    // create new region
    String updatedName = "updated name";
    String updatedSymbol = "updated symbol";
    String updatedDescription = "updated description";
    Map<String, Object> attributeMap = new ImmutableMap.Builder<String, Object>()
        .put("name", "test name")
        .put("description", "test description")
        .put("symbol", "test symbol").build();

    int id = testCreate("/region", toJsonAPIMap("region", attributeMap));

    // update
    Map<String, Object> attributeMapUpdate = new ImmutableMap.Builder<String, Object>()
        .put("name", updatedName)
        .put("description", updatedDescription)
        .put("symbol", updatedSymbol).build();
    testUpdate("/region/" + id, toJsonAPIMap("region", attributeMapUpdate, id));

    loadJsonApiSchema("json-schema/regionJSONSchema.json");
    ValidatableResponse responseUpdate = testFindOne("/region/" + id);

    // verify
    responseUpdate.body("data.attributes.name", equalTo(updatedName))
        .body("data.attributes.symbol", equalTo(updatedSymbol))
        .body("data.attributes.description", equalTo(updatedDescription));
  }

}
