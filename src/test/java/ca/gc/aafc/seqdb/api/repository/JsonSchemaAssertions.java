package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
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

/**
 * Utility class to assert an api response against a json schema.
 * 
 */
public class JsonSchemaAssertions {

  private static final Consumer<String> ASSERTION_PRINTER = ((error) -> assertNull(
      "Validation service is not returning errors", error));

  private JsonSchemaAssertions() {
  }

  /**
   * Assert that the provided apiResponse validates against the provided schema.
   * 
   * @param jsonSchema the json schema
   * @param apiResponse the api response
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
   * Schema resolver which looks for $ref locations in our resources where all of
   * schemas are located.
   * This may be a temporary solution.
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
