package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.springframework.http.HttpStatus;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.seqdb.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.entities.PreLibraryPrep.PreLibraryPrepType;
import ca.gc.aafc.seqdb.entities.Protocol.ProtocolType;
import ca.gc.aafc.seqdb.factories.PreLibraryPrepFactory;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PreLibraryPrepJsonApiIT extends BaseJsonApiIntegrationTest {

  private int protocolId;
  private int productId;
  
  @Override
  protected String getResourceUnderTest() {
    return "preLibraryPrep";
  }
  
  @Override
  protected String getGetOneSchemaFilename() {
    return "getOnePreLibraryPrepSchema.json";
  }

  @Override
  protected String getGetManySchemaFilename() {
    return "getManyPreLibraryPrepSchema.json";
  }
  
  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    PreLibraryPrep preLibraryPrep = PreLibraryPrepFactory.newPreLibraryPrep()
        .preLibraryPrepType(PreLibraryPrepType.SHEARING)
        .notes("Test Notes")
        .quality("Test Quality").build();

    Map<String, Object> map = toAttributeMap(preLibraryPrep);
    // we should not be able to provide those fields but the API currently allows it
    map.remove("lft");
    map.remove("rgt");
    
    return map;
  }
  
  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    PreLibraryPrep preLibraryPrep = PreLibraryPrepFactory.newPreLibraryPrep()
        .preLibraryPrepType(PreLibraryPrepType.SIZE_SELECTION)
        .notes("Updated Notes")
        .quality("Updated Quality").build();

    Map<String, Object> map = toAttributeMap(preLibraryPrep);
    // we should not be able to provide those fields but the API currently allows it
    map.remove("lft");
    map.remove("rgt");
    
    return map;
  }

  /**
   * In order to test out the relationships, we need to first insert them into the
   * database so we can test if it can properly link and retrieve it. 
   */
  @Before
  public void buildRelationshipInstances() {
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
    Response protocolResponse = given().contentType(JSON_API_CONTENT_TYPE)
        .body(protocolMap).when().post("protocol");
    //Stores id found in json response to be used in reaction component post and destroyProtocol
    String strid = (String) protocolResponse.body().jsonPath().get("data.id");
    protocolId = Integer.parseInt(strid);
    
    // Build attributes for product
    ImmutableMap.Builder<String, Object> productAttributes = new ImmutableMap.Builder<>();
    productAttributes.put("name", "test product").build();
    
    // Create map for product. 
    Map<String, Object> productMap = toJsonAPIMap("product", productAttributes.build(), null, null);
    
    //Post the json map and expect a successful http status
    Response productResponse = given().contentType(JSON_API_CONTENT_TYPE)
        .body(productMap).when().post("product");
    
    //Stores id found in json response to be used in reaction component post and destroyProtocol
    strid = (String) productResponse.body().jsonPath().get("data.id");
    productId = Integer.parseInt(strid);
  }
  
  /**
   * To avoid conflict with the rest of the tests, the protocol and products should be
   * deleted. 
   */
  @After
  public void destroyRelationshipInstances() {
    // Delete protocol.
    given().contentType(JSON_API_CONTENT_TYPE).when().delete("protocol" + "/" + protocolId)
    .then().statusCode(HttpStatus.NO_CONTENT.value());
    
    // Delete product.
    given().contentType(JSON_API_CONTENT_TYPE).when().delete("product" + "/" + productId)
    .then().statusCode(HttpStatus.NO_CONTENT.value());
  }
  
  @Override
  protected Map<String, Object> buildRelationshipMap() {
    // Test protocol relationship.
    ImmutableMap.Builder<String, Object> protocolRelationship = new ImmutableMap.Builder<>();
    protocolRelationship.put("type", "protocol")
                        .put("id", String.valueOf(protocolId))
                        .build();

    ImmutableMap.Builder<String, Object> protocolBldr = new ImmutableMap.Builder<>();
    protocolBldr.put("data", protocolRelationship.build());
    
    // Test product relationship.
    ImmutableMap.Builder<String, Object> productRelationship = new ImmutableMap.Builder<>();
    productRelationship.put("type", "product")
                        .put("id", String.valueOf(productId))
                        .build();

    ImmutableMap.Builder<String, Object> productBldr = new ImmutableMap.Builder<>();
    productBldr.put("data", productRelationship.build());
    
    // Put the two relationships together to create all of the relationships to test.
    ImmutableMap.Builder<String, Object> relationshipBldr = new ImmutableMap.Builder<>();
    relationshipBldr.put("protocol", protocolBldr.build());
    relationshipBldr.put("product", productBldr.build());
    
    return relationshipBldr.build();
  }
  
}
