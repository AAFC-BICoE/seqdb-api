package ca.gc.aafc.seqdb.api;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TomcatWebServerCustomizerIT extends BaseHttpIntegrationTest {

  /**
   * Test to ensure that square brackets are allowed in URLs.
   * @throws IOException 
   * @throws ClientProtocolException 
   */
  @Test
  public void sendRequestToRegionEndpoint_whenUrlHasSquareBrackets_statusCode401() throws ClientProtocolException, IOException {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet("http://localhost:"+ testPort + "/api/region?page[limit]=10");
    HttpResponse response = client.execute(request);
    // Expect status code 401 unauthorized, instead of 400 for illegal square brackets.
    assertEquals(401, response.getStatusLine().getStatusCode());
  }

  @Test
  public void sendRequestToRegionEndpoint_withAuthentication_statusCode200()
       throws ClientProtocolException, IOException {
    
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet("http://localhost:" + testPort + "/api/region?page[limit]=10");

    String auth = "Admin:Admin";

    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
    String authHeader = "Basic " + new String(encodedAuth);
    request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

    HttpResponse response = client.execute(request);
    // Expect status code 200 unauthorized.
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

}
