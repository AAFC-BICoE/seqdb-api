package ca.gc.aafc.seqdb.api.entity;

import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@Transactional
public class ProductTest extends TestCase {

  private static String endPoint;
  
  private int testProductID;
  
  private RequestSpecification givenAuth() {
    return RestAssured.given().port(port)
        .auth().preemptive()
        .basic("Admin", "Admin");
 }
  
  @LocalServerPort
  private int port;
  
  @Before
  public void setup() throws ClientProtocolException, IOException, URISyntaxException, JSONException {
    endPoint = "/api";
    createTestProduct();    
  }
  
  @Test
  public void givenProductDoesExist_whenProductIsRetrieved_then201IsReceived()
      throws ClientProtocolException, IOException {
    
    final Response response = givenAuth().get(endPoint + "/product?filter[name]=myName");
    assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    
    List<Object> ids = response.jsonPath().getList("data.id");
    assertNotNull(ids.get(0));
    testProductID = (int)Integer.parseInt((String) ids.get(0));
 }  
  
  @Test
  public void givenProductDoesNotExists_whenProductIsRetrieved_then404IsReceived()
      throws ClientProtocolException, IOException {
    
    final Response response = givenAuth().get(endPoint + "/product/90001");
    assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void createProduct_whenRequestIsExecuted_then200IsReceived()
      throws ClientProtocolException, IOException, URISyntaxException, JSONException {
    Map<String, Object> attributeMap = new ImmutableMap.Builder<String, Object>()
        .put("name", "Shemy").put("type", "type").put("description", "desc").build();

    @SuppressWarnings("rawtypes")
    Map dataMap = ImmutableMap.of("data", ImmutableMap.of("type", "product", "id", 900001, "attributes", attributeMap));

    givenAuth().contentType("application/vnd.api+json").body(dataMap).when().post
        (endPoint+ "/product").then().statusCode(HttpStatus.SC_CREATED);

  }

  @Test
  public void givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
      throws ClientProtocolException, IOException {
    givenProductDoesExist_whenProductIsRetrieved_then201IsReceived();
    assertNotEquals(0, testProductID);
    
    String jsonMimeType = "application/json;charset=UTF-8";
    final Response response = givenAuth().get(endPoint + "/products/" + testProductID);
    
    String mimeType = response.getContentType();
    assertEquals(jsonMimeType, mimeType);
  }

  @Test
  public void updateProduct_whenRequestIsExecuted_then201IsReceived() throws ClientProtocolException, IOException {
   givenProductDoesExist_whenProductIsRetrieved_then201IsReceived();
   assertNotEquals(0, testProductID);
  
   Map<String, Object> attributeMap = new ImmutableMap.Builder<String, Object>()
        .put("description", "desc1").build();

    @SuppressWarnings("rawtypes")
    Map dataMap = ImmutableMap.of("data", ImmutableMap.of("type", "product", "id", testProductID, "attributes", attributeMap));

    givenAuth().contentType("application/vnd.api+json").body(dataMap).when().patch(endPoint + "/product/" + testProductID).then()
              .statusCode(HttpStatus.SC_OK);
  }
  
  @Test
  public void deleteProduct_whenRequestIsExecuted_then200IsReceived() throws ClientProtocolException, IOException {
    givenProductDoesExist_whenProductIsRetrieved_then201IsReceived();
    assertNotEquals(0, testProductID);
    
    Map<String, Object> attributeMap = new ImmutableMap.Builder<String, Object>()
        .put("name", "myName").build();

    @SuppressWarnings("rawtypes")
    Map dataMap = ImmutableMap.of("data", ImmutableMap.of("type", "product", "id", testProductID, "attributes", attributeMap));

    givenAuth().contentType("application/vnd.api+json").body(dataMap).when().patch(endPoint + "/product/" + testProductID).then()
              .statusCode(HttpStatus.SC_OK);
  }
  
  public void createTestProduct()
      throws ClientProtocolException, IOException, URISyntaxException, JSONException {
    
    Map<String, Object> attributeMap = new ImmutableMap.Builder<String, Object>()
        .put("name", "myName").put("type", "type").put("description", "desc").build();

    @SuppressWarnings("rawtypes")
    Map dataMap = ImmutableMap.of("data", ImmutableMap.of("type", "product", "id", 900001, "attributes", attributeMap));

    givenAuth().contentType("application/vnd.api+json").body(dataMap).when().post
        (endPoint+ "/product").then().statusCode(HttpStatus.SC_CREATED);

  }
}
