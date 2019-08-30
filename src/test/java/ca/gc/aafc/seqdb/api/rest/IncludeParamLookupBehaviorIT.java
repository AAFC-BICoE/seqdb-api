package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.seqdb.api.BaseHttpIntegrationTest;
import ca.gc.aafc.seqdb.api.security.ImportSampleAccounts;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IncludeParamLookupBehaviorIT extends BaseHttpIntegrationTest {
  
  public static final String JSON_API_CONTENT_TYPE = BaseJsonApiIntegrationTest.JSON_API_CONTENT_TYPE;
  
  @BeforeEach
  public final void before() {
    RestAssured.port = testPort;
    RestAssured.baseURI = BaseJsonApiIntegrationTest.IT_BASE_URI.toString();
    RestAssured.basePath = BaseJsonApiIntegrationTest.API_BASE_PATH;
    
    //set basic auth with ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME
    PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
    authScheme.setUserName(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME);
    authScheme.setPassword(ImportSampleAccounts.IMPORTED_USER_ACCOUNT_NAME);
    RestAssured.authentication = authScheme;
  }
  
  @Test
  public void getPcrPrimer_whenIncludedRegionIsNull_no404ErrorIsGiven() {
    // Create a test primer.
    Map<String, Object> testPrimerAttributeMap = new ImmutableMap.Builder<String, Object>()
        .put("name", "test primer for issue #16778")
        .put("lotNumber", 1)
        .put("type", "PRIMER")
        .put("seq", "").build();
    
    Map<String, Object> body = BaseJsonApiIntegrationTest
        .toJsonAPIMap("pcrPrimer", testPrimerAttributeMap, null, null);
    Response primerCreateResponse = given().contentType(JSON_API_CONTENT_TYPE).body(body)
        .post("pcrPrimer");
    assertEquals(HttpStatus.CREATED.value(), primerCreateResponse.getStatusCode());

    // Get the created primer's ID.
    String newPrimerId = primerCreateResponse.body().path("data.id");
    
    // Request the new primer with the included region (which is null).
    Response primerGetResponse = given().contentType(JSON_API_CONTENT_TYPE)
        .get(String.format("pcrPrimer/%s?include=region", newPrimerId));
    
    // The included region should be null.
    assertEquals(200, primerGetResponse.statusCode());
    assertNull(primerGetResponse.body().path("data.relationships.region.data"));
    
    // Cleanup the created primer.
    Response deleteResponse = given().contentType(JSON_API_CONTENT_TYPE)
        .delete(String.format("pcrPrimer/%s", newPrimerId));
    assertEquals(HttpStatus.NO_CONTENT.value(), deleteResponse.statusCode());
  }
  
}
