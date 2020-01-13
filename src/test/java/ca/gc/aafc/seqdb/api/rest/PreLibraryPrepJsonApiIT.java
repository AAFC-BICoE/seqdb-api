package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.seqdb.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.entities.PreLibraryPrep.PreLibraryPrepType;
import ca.gc.aafc.seqdb.entities.Protocol.ProtocolType;
import ca.gc.aafc.seqdb.testsupport.factories.PreLibraryPrepFactory;

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
    return toAttributeMap(preLibraryPrep);
  }
  
  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    PreLibraryPrep preLibraryPrep = PreLibraryPrepFactory.newPreLibraryPrep()
        .preLibraryPrepType(PreLibraryPrepType.SIZE_SELECTION)
        .notes("Updated Notes")
        .quality("Updated Quality").build();
    return toAttributeMap(preLibraryPrep);
  }

  /**
   * In order to test out the relationships, we need to first insert them into the
   * database so we can test if it can properly link and retrieve it. 
   */
  @BeforeEach
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
    protocolId = sendPost("protocol", protocolMap);
    
    // Build attributes for product
    ImmutableMap.Builder<String, Object> productAttributes = new ImmutableMap.Builder<>();
    productAttributes.put("name", "test product").build();
    // Create map for product. 
    Map<String, Object> productMap = toJsonAPIMap("product", productAttributes.build(), null, null);
    productId = sendPost("product", productMap);
  }
  
  /**
   * To avoid conflict with the rest of the tests, the protocol and products should be
   * deleted. 
   */
  @AfterEach
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
