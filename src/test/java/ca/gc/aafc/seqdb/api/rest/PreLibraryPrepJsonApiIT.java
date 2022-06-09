package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.commons.annotation.Testable;
import org.springframework.http.HttpStatus;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepType;
import ca.gc.aafc.seqdb.api.testsupport.factories.PreLibraryPrepFactory;

@Testable
public class PreLibraryPrepJsonApiIT extends BaseJsonApiIntegrationTest {

  private String productId;
  
  @Override
  protected String getResourceUnderTest() {
    return "pre-library-prep";
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
    // Build attributes for product
    ImmutableMap.Builder<String, Object> productAttributes = new ImmutableMap.Builder<>();
    productAttributes.put("name", "test product").build();
    // Create map for product. 
    Map<String, Object> productMap = toJsonAPIMap("product", productAttributes.build(), null, null);
    productId = sendPost("product", productMap);
  }
  
  /**
   * To avoid conflict with the rest of the tests, the products should be
   * deleted. 
   */
  @AfterEach
  public void destroyRelationshipInstances() {
    // Delete product.
    given().contentType(JSON_API_CONTENT_TYPE).when().delete("product" + "/" + productId)
    .then().statusCode(HttpStatus.NO_CONTENT.value());
  }
  
  @Override
  protected Map<String, Object> buildRelationshipMap() {
    // Test external relationship protocol
    ImmutableMap.Builder<String, Object> protocolRelationship = new ImmutableMap.Builder<>();
    protocolRelationship.put("type", "protocol")
                        .put("id", UUID.randomUUID().toString())
                        .build();

    // Test product relationship.
    ImmutableMap.Builder<String, Object> productRelationship = new ImmutableMap.Builder<>();
    productRelationship.put("type", "product")
                        .put("id", String.valueOf(productId))
                        .build();

    ImmutableMap.Builder<String, Object> productBldr = new ImmutableMap.Builder<>();
    productBldr.put("data", productRelationship.build());
    
    // Put the two relationships together to create all of the relationships to test.
    ImmutableMap.Builder<String, Object> relationshipBldr = new ImmutableMap.Builder<>();
    relationshipBldr.put("product", productBldr.build());
    relationshipBldr.put("protocol", protocolRelationship.build());
    
    return relationshipBldr.build();
  }
  
}
