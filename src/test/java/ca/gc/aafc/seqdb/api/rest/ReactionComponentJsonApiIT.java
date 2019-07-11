package ca.gc.aafc.seqdb.api.rest;

import ca.gc.aafc.seqdb.entities.Protocol.ProtocolType;
import com.google.common.collect.ImmutableMap;
import io.restassured.response.Response;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

/**
 * Integration test for the reaction component resource.
 */
public class ReactionComponentJsonApiIT extends BaseJsonApiIntegrationTest {

  private int id;

  @Override
  protected String getResourceUnderTest() {
    return "reactionComponent";
  }

  @Override
  protected String getGetOneSchemaFilename() {
    return "reactionComponentJSONSchema.json";
  }

  @Override
  protected String getGetManySchemaFilename() {
    return "GETreactionComponentJSONSchema.json";
  }

  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
      .put("name", "test component")
      .put("concentration", "20mg/mL")
      .put("quantity", "6.82").build();
  }

  @Override
  protected Map<String, Object> buildRelationshipMap() {
    ImmutableMap.Builder<String, Object> relationships = new ImmutableMap.Builder<>();
    relationships.put("type", "protocol").put("id", String.valueOf(id)).build();

    ImmutableMap.Builder<String, Object> bldr = new ImmutableMap.Builder<>();
    bldr.put("data", relationships.build());
    return ImmutableMap.of("protocol", bldr.build());
  }

  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
      .put("name", "new test component")
      .put("concentration", "45mg/mL")
      .put("quantity", "9.37").build();
  }

  /*
   * Builds and posts a protocol required to post a reaction component.
   */
  @Before
  public void buildProtocol() {
    //Build attributes for protocol
    ImmutableMap.Builder<String, Object> protocolAttributes = new ImmutableMap.Builder<>();
    protocolAttributes.put("name", "test protocol")
      .put("type", ProtocolType.COLLECTION_EVENT)
      .put("version", "A")
      .build();

    //Build group relationship for protocol
    ImmutableMap.Builder<String, Object> protocolRelationships = new ImmutableMap.Builder<>();
    protocolRelationships.put("type", "group").put("id", "2").build();

    ImmutableMap.Builder<String, Object> dataBldr = new ImmutableMap.Builder<>();
    dataBldr.put("data", protocolRelationships.build());

    ImmutableMap.Builder<String, Object> relationshipBldr = new ImmutableMap.Builder<>();
    relationshipBldr.put("group", dataBldr.build());

    //Put maps together and create one json map
    Map<String, Object> protocolMap = toJsonAPIMap(
        "protocol", protocolAttributes.build(), relationshipBldr.build(), null);
    //Post the json map and expect a successful http status
    Response response = given().contentType(JSON_API_CONTENT_TYPE)
        .body(protocolMap).when().post("protocol");
    //Stores id found in json response to be used in reaction component post and destroyProtocol
    String strid = (String) response.body().jsonPath().get("data.id");
    id = Integer.parseInt(strid);
  }

  /*
   * Destroys the protocol when the test is complete.
   */
  @After
  public void destroyProtocol() {
    //Remove the protocol after each test to avoid constraint violations
    given().contentType(JSON_API_CONTENT_TYPE).when().delete("protocol" + "/" + id)
    .then().statusCode(HttpStatus.NO_CONTENT.value());
  }
}
