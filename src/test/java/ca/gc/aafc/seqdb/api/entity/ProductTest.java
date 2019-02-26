package ca.gc.aafc.seqdb.api.entity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeqdbApiLauncher.class)
@ContextConfiguration
@Transactional
public class ProductTest extends TestCase {

  private static String endPoint;
  
  @PersistenceContext
  protected EntityManager entityManager;  
  
  
  private RequestSpecification givenAuth() {
    return RestAssured.given()
        .auth().preemptive()
        .basic("reader", "reader");
 }
  
  @Before
  public void setup() {
    endPoint = "/api";
 
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
        .put("name", "myName").put("type", "type").put("description", "desc").build();

    @SuppressWarnings("rawtypes")
    Map dataMap = ImmutableMap.of("data", ImmutableMap.of("type", "product", "id", 900001, "attributes", attributeMap));

    givenAuth().contentType("application/vnd.api+json").body(dataMap).when().post
        (endPoint+ "/product").then().statusCode(HttpStatus.SC_CREATED);

  }

  @Test
  public void givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson()
      throws ClientProtocolException, IOException {

    String jsonMimeType = "application/json;charset=UTF-8";
    final Response response = givenAuth().get(endPoint + "/products/1");
    
    String mimeType = response.getContentType();
    assertEquals(jsonMimeType, mimeType);
  }
  
  @Test
  public void givenProductDoesExist_whenProductIsRetrieved_then201IsReceived()
      throws ClientProtocolException, IOException {
    
    final Response response = givenAuth().get(endPoint + "/product/1");
    assertEquals(HttpStatus.SC_OK, response.getStatusCode());
  }
  
  @Test
  public void updateProduct_whenRequestIsExecuted_then201IsReceived() {
   Map<String, Object> attributeMap = new ImmutableMap.Builder<String, Object>()
        .put("description", "desc1").build();

    @SuppressWarnings("rawtypes")
    Map dataMap = ImmutableMap.of("data", ImmutableMap.of("type", "product", "id", 1, "attributes", attributeMap));

    givenAuth().contentType("application/vnd.api+json").body(dataMap).when().patch(endPoint + "/product/1").then()
              .statusCode(HttpStatus.SC_OK);
  }  
}
