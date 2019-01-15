package ca.gc.aafc.seqdb.api;

import java.io.IOException;
import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    classes = SeqdbApiLauncher.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {
        "server.port=8080",
        "import-sample-accounts=true"
    }
)
public class TomcatWebServerCustomizerIT extends BaseIntegrationTest {

  /**
   * Test to ensure that square brackets are allowed in URLs.
   * @throws IOException 
   * @throws ClientProtocolException 
   */
  @Test
  public void sendRequestToRegionEndpoint_whenUrlHasSquareBrackets_statusCode200() throws ClientProtocolException, IOException {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet("http://localhost:8080/api/region?page[limit]=10");
    request.setHeader(
        "Authorization",
        "Basic " + Base64.getEncoder().encodeToString(("Admin:Admin").getBytes())
    );
    HttpResponse response = client.execute(request);
    assertEquals(200, response.getStatusLine().getStatusCode());
  }
  
}
