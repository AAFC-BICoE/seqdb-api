package ca.gc.aafc.seqdb.api.repository;

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.function.Consumer;

import javax.json.stream.JsonParser;

import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;
import org.springframework.core.io.ClassPathResource;

public class JsonSchemaAssertions {

  private static final Consumer<String> ASSERTION_PRINTER  = ((error) -> assertNull("Validation service is not returning errors", error));

  public static void assertJsonSchema(String jsonSchemaPath, String apiResponse)
      throws IOException {

    JsonValidationService service = JsonValidationService.newInstance();
    // Reads the JSON schema
    JsonSchema schema = service
        .readSchema(new ClassPathResource(jsonSchemaPath).getFile().toPath());
    // Problem handler which will print problems found.
    ProblemHandler handler = service.createProblemPrinter(ASSERTION_PRINTER);
    // Parses the JSON instance by javax.json.stream.JsonParser
    try (JsonParser parser = service
        .createParser(new ClassPathResource(apiResponse).getFile().toPath(), schema, handler)) {
      while (parser.hasNext()) {
        parser.next();
      }
    }

  }
}
