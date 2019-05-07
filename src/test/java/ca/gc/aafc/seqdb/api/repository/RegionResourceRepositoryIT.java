package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;

import javax.json.stream.JsonParser;

import org.junit.Test;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;
import org.springframework.core.io.ClassPathResource;

public class RegionResourceRepositoryIT extends BaseRepositoryTest{

  @Test
  public void findAllRegions_Validation() throws IOException {
    JsonValidationService service = JsonValidationService.newInstance();
    // Reads the JSON schema
    JsonSchema schema = service.readSchema(new ClassPathResource("schema/GETregionJSONSchema.json").getFile().toPath());
    // Problem handler which will print problems found.
    ProblemHandler handler = service.createProblemPrinter(System.out::println);
    // Parses the JSON instance by javax.json.stream.JsonParser
    try (JsonParser parser = service.createParser(new ClassPathResource("schema/realRegionResponse-all.json").getFile().toPath(), schema, handler)) {
      while (parser.hasNext()) {
        JsonParser.Event event = parser.next();
         System.out.println("event is " + event.toString());
      }
    }
  }  
}
