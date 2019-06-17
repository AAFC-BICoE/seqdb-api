package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;

import org.junit.Test;

public class RegionResourceRepositoryIT {

  @Test
  public void listRegion_APIResponse_schemaValidates() throws IOException {

    JsonSchemaAssertions.assertJsonSchema(
        BaseRepositoryTest.newClasspathResourceReader("static/json-schema/GETregionJSONSchema.json"),
        BaseRepositoryTest.newClasspathResourceReader("realRegionResponse-all.json"));
  }

  @Test
  public void getRegion_APIResponse_schemaValidates() throws IOException {
    JsonSchemaAssertions.assertJsonSchema(
        BaseRepositoryTest.newClasspathResourceReader("static/json-schema/regionJSONSchema.json"),
        BaseRepositoryTest.newClasspathResourceReader("realRegionResponse.json"));
  }
}
