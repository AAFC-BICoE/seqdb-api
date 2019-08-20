package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.json.JsonException;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParsingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonSchemaReader;
import org.leadpony.justify.api.JsonSchemaReaderFactory;
import org.leadpony.justify.api.JsonSchemaResolver;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;
import org.leadpony.justify.api.SpecVersion;

/**
 * Utility class to assert an api response against a json schema.
 * 
 */
public final class JsonSchemaAssertions {
  
  private static final JsonValidationService JSON_VALIDATION_SERVICE = JsonValidationService.newInstance();
  private static final Consumer<String> ASSERTION_PRINTER = error -> assertNull(
      "Validation service is not returning errors", error);
  
  private JsonSchemaAssertions() {
  }

  /**
   * Assert that the provided apiResponse validates against the provided schema.
   * 
   * @param jsonSchema
   *          the json schema
   * @param apiResponse
   *          the api response
   * @throws IOException
   */
  public static void assertJsonSchema(Reader jsonSchema, Reader apiResponse) throws IOException {

    JsonValidationService service = JsonValidationService.newInstance();

    // Create a custom JSON reader
    JsonSchemaReaderFactory factory = service.createSchemaReaderFactoryBuilder()
        .withSchemaResolver(new LocalJsonSchemaResolver(service)).build();

    // Reads the JSON schema
    JsonSchemaReader reader = factory.createSchemaReader(jsonSchema);

    // Generate the schema object from the reader.
    JsonSchema schema = reader.read();

    // Problem handler which will print problems found.
    ProblemHandler handler = service.createProblemPrinter(ASSERTION_PRINTER);

    // Parses the JSON instance by javax.json.stream.JsonParser
    try (JsonParser parser = service.createParser(apiResponse, schema, handler)) {
      while (parser.hasNext()) {
        parser.next();
      }
    }

  }
  
  /**
   * Assert that the provided apiResponse validates against the provided schema URI.
   * 
   * @param uri
   *          the uri of the json schema
   * @param apiResponse
   *          the api response
   */
  public static void assertJsonSchema(URI uri, Reader apiResponse) {

    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      JsonSchema schema = new NetworkJsonSchemaResolver(uri.getPort(), httpClient)
          .resolveSchema(uri);

      // Problem handler which will print problems found.
      ProblemHandler handler = JSON_VALIDATION_SERVICE.createProblemPrinter(ASSERTION_PRINTER);

      // Parses the JSON instance by javax.json.stream.JsonParser
      try (JsonParser parser = JSON_VALIDATION_SERVICE.createParser(apiResponse, schema, handler)) {
        while (parser.hasNext()) {
          parser.next();
        }
      }
    } catch (JsonException jsonEx) {
      fail("Trying to assert schema located at " + uri.toString() + ": " + jsonEx.getMessage());
    } finally {
      try {
        if (httpClient != null) {
          httpClient.close();
        }
      } catch (IOException e) {
        // ignore
      }
    }
  }

  /**
   * Schema resolver which resolves remote url for $ref locations in our resources where 
   * subschemas are located. 
   */
  private static final class NetworkJsonSchemaResolver implements JsonSchemaResolver {

    private final int portUsed;
    private final JsonSchemaReaderFactory schemaReaderFactory;
    private final CloseableHttpClient httpClient;
    
    /**
     * 
     * @param portUsed used to dynamically change the port on recursive calls to load sub-schemas
     */
    private NetworkJsonSchemaResolver(int portUsed, CloseableHttpClient httpClient){
      this.portUsed = portUsed;
      this.httpClient = httpClient;
      this.schemaReaderFactory = JSON_VALIDATION_SERVICE.createSchemaReaderFactoryBuilder()
          .withSchemaResolver(this)
          .withDefaultSpecVersion(SpecVersion.DRAFT_07).build();
    }
    
    @Override
    public JsonSchema resolveSchema(URI uri) {
      URI testResolvableUri = null;
      URIBuilder uriBuilder = new URIBuilder(uri);
      uriBuilder.setPort(portUsed);
      String responseBody;

      try {
        testResolvableUri = uriBuilder.build();
        HttpGet httpget = new HttpGet(testResolvableUri);
        responseBody = httpClient.execute(httpget, builResponseHandler());
      } catch (URISyntaxException | IOException e) {
        fail(e.getMessage());
        return null;
      }
      
      try (JsonSchemaReader reader = schemaReaderFactory
          .createSchemaReader(new StringReader(responseBody))) {
        return reader.read();
      } catch (JsonParsingException e) {
        fail("Error while trying to read schema from " + testResolvableUri.toString() + ":" + e.getMessage());
        throw e;
      }
    }
  }

  
  private static ResponseHandler<String> builResponseHandler() {
    return new ResponseHandler<String>() {
      @Override
      public String handleResponse(HttpResponse response)
          throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
          HttpEntity entity = response.getEntity();
          return entity != null ? EntityUtils.toString(entity) : null;
        } else {
          throw new ClientProtocolException("Unexpected response status: " + status);
        }

      }
    };
  }
  
  /**
   * Schema resolver which looks for $ref locations in our resources where all of schemas are
   * located. This may be a temporary solution.
   */
  private static class LocalJsonSchemaResolver implements JsonSchemaResolver {

    private final JsonValidationService service;

    LocalJsonSchemaResolver(JsonValidationService service) {
      this.service = service;
    }

    @Override
    public JsonSchema resolveSchema(URI id) {
      Path path = null;

      // Try to find the path based on where all of the schemas are located.
      try {

        // Grab the resource the schema is trying to reference.
        URL resource = ClassLoader.getSystemResource(id.getPath());

        // Check if it exists. If no resource is found, return no schema back.
        if (resource == null) {
          return null;
        }

        // Get the schemas path based on the resource given.
        path = Paths.get(resource.toURI());

      } catch (URISyntaxException e) {
        fail(e.getMessage());
        return null;
      }
      return service.readSchema(path);
    }
  };

}
