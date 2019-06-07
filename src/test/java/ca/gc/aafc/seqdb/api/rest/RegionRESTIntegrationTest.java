package ca.gc.aafc.seqdb.api.rest;

import java.io.IOException;
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

  @Override
  protected String getResourceUnderTest() {
    return "region";
  }
  
  @Override
  protected String getGetOneSchemaFilename() {
    return "regionJSONSchema.json";
  }

  @Override
  protected String getGetManySchemaFilename() {
    return "GETregionJSONSchema.json";
  }
  
  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
        .put("name", "test region")
        .put("description", "test description")
        .put("symbol", "test symbol").build();
  }

  @Test
  public void testUpdateTask() {
    // create new region
    String updatedName = "updated name";
    String updatedSymbol = "updated symbol";
    String updatedDescription = "updated description";

    int id = sendPost(toJsonAPIMap("region", buildCreateAttributeMap()));

    // update
    Map<String, Object> attributeMapUpdate = new ImmutableMap.Builder<String, Object>()
        .put("name", updatedName)
        .put("description", updatedDescription)
        .put("symbol", updatedSymbol).build();
    testUpdate("/region/" + id, toJsonAPIMap("region", attributeMapUpdate, id));

    ValidatableResponse responseUpdate = sendGet(id);

    // verify
    responseUpdate.body("data.attributes.name", equalTo(updatedName))
        .body("data.attributes.symbol", equalTo(updatedSymbol))
        .body("data.attributes.description", equalTo(updatedDescription));
  }

}
