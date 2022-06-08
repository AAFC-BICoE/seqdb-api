package ca.gc.aafc.seqdb.api.rest;

import static io.restassured.RestAssured.given;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularSampleFactory;

public class MolecularSampleJsonApiIT extends BaseJsonApiIntegrationTest {

  private String productId;

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
   * To avoid conflict with the rest of the tests, the protocol and products should be
   * deleted. 
   */
  @AfterEach
  public void destroyRelationshipInstances() {
    // Delete product.
    given().contentType(JSON_API_CONTENT_TYPE).when().delete("product" + "/" + productId)
    .then().statusCode(HttpStatus.NO_CONTENT.value());
  }
  
  @Override
  protected String getResourceUnderTest() {
    return "molecular-sample";
  }

  @Override
  protected Map<String, Object> buildCreateAttributeMap() {

    MolecularSample molecularSample = MolecularSampleFactory.newMolecularSample()
      .build();
    
    return toAttributeMap(molecularSample);
  }

  @Override
  protected Map<String, Object> buildRelationshipMap() {
    // Test external relationship material-sample
    ImmutableMap.Builder<String, Object> materialSampleRelationship = new ImmutableMap.Builder<>();
    materialSampleRelationship.put("type", "material-sample")
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
    relationshipBldr.put("kit", productBldr.build());
    relationshipBldr.put("materialSample", materialSampleRelationship.build());
    
    return relationshipBldr.build();
  }

  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {

    MolecularSample molecularSample = MolecularSampleFactory.newMolecularSample()
      .build();
    
    return toAttributeMap(molecularSample);
  }

}
