package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;
import java.util.function.Consumer;

import javax.json.stream.JsonParser;

import org.junit.Test;
import org.leadpony.justify.api.JsonSchema;
import org.leadpony.justify.api.JsonValidationService;
import org.leadpony.justify.api.ProblemHandler;
import org.springframework.core.io.ClassPathResource;

public class RegionResourceRepositoryIT extends BaseRepositoryTest{

  @Test
  public void findAllRegions_Validation() throws IOException {
    Consumer <String> assertionPrinter = ( (error)->assertNull(error));
    
    JsonValidationService service = JsonValidationService.newInstance();
    // Reads the JSON schema
    JsonSchema schema = service.readSchema(
        new ClassPathResource("json-schema/GETregionJSONSchema.json").
        getFile().toPath());
    // Problem handler which will print problems found.
    ProblemHandler handler = service.createProblemPrinter(
        assertionPrinter);
    // Parses the JSON instance by javax.json.stream.JsonParser
    try (JsonParser parser = service.createParser(
        new ClassPathResource("realRegionResponse-all.json").
        getFile().toPath(), schema, handler)) {
      while (parser.hasNext()) {
        parser.next();
      }
    }
    
    service = JsonValidationService.newInstance();    
    schema = service.readSchema(
        new ClassPathResource("json-schema/regionJSONSchema.json").
        getFile().toPath());
    handler = service.createProblemPrinter(
        assertionPrinter);    
    try (JsonParser parser = service.createParser(
        new ClassPathResource("realRegionResponse.json").
        getFile().toPath(), schema, handler)) {
      while (parser.hasNext()) {
        parser.next();
      }
    }
  }  
}
