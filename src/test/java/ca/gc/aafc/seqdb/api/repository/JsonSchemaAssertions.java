package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.json.stream.JsonParser;

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
public class JsonSchemaAssertions {
  private static JsonValidationService service = JsonValidationService.newInstance();
  private static JsonSchemaReaderFactory schemaReaderFactory;
  
  private static String testPort;

  private static final Consumer<String> ASSERTION_PRINTER = ((error) -> assertNull(
      "Validation service is not returning errors", error));

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
   * Assert that the provided apiResponse validates against the provided schema with remote url ref
   * 
   * @param uriStr
   *          the uri as string
   * @param apiResponse
   *          the api response
   * @throws IOException
   * @throws URISyntaxException
   */
  public static void assertJsonSchema_Network(String uriStr, Reader apiResponse,String port)
      throws IOException, URISyntaxException {
    testPort = port;
    URI uri = new URI(uriStr);
    // Build reader factory which will resolve provided schema version using the given resolver
    schemaReaderFactory = service.createSchemaReaderFactoryBuilder()
        .withSchemaResolver(new NetworkJsonSchemaResolver())
        .withDefaultSpecVersion(SpecVersion.DRAFT_07).build();

    JsonSchema schema = new NetworkJsonSchemaResolver().resolveSchema(uri);

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
   * Schema resolver which resolves remote url for $ref locations in our resources where subschemas are located. 
   */
  private static class NetworkJsonSchemaResolver implements JsonSchemaResolver {

    @Override
    public JsonSchema resolveSchema(URI uri) {
      String urlStr;
      URL url = null;
    
      try {
        urlStr = uri.toURL().toString().replace("8080", testPort);
        url = new URL(urlStr);        
      } catch (MalformedURLException e1) {
        return null;
      }

      try (InputStream in = url.openStream();
                JsonSchemaReader reader = schemaReaderFactory.createSchemaReader(in)) {          
        return reader.read();
      } catch (MalformedURLException e) {
         return null;
      } catch (IOException e) {
         return null;
      }
    }
  }

  /**
   * Schema resolver which looks for $ref locations in our resources where all of schemas are
   * located. This may be a temporary solution.
   */
  private static class LocalJsonSchemaResolver implements JsonSchemaResolver {

    private final JsonValidationService service;

    public LocalJsonSchemaResolver(JsonValidationService service) {
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
