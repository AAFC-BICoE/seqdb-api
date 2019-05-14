package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Consumer;

import javax.json.stream.JsonParser;

import org.leadpony.justify.api.JsonSchema;
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
    // Reads the JSON schema
    JsonSchema schema = service.readSchema(jsonSchema);
    // Problem handler which will print problems found.
    ProblemHandler handler = service.createProblemPrinter(ASSERTION_PRINTER);
    // Parses the JSON instance by javax.json.stream.JsonParser
    try (JsonParser parser = service.createParser(apiResponse, schema, handler)) {
      while (parser.hasNext()) {
        parser.next();
      }
    }

  }
}
