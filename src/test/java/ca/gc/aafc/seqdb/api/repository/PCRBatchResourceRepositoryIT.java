package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class PCRBatchResourceRepositoryIT {

  @Test
  public void listRegion_APIResponse_schemaValidates() throws IOException {
    JsonSchemaAssertions.assertJsonSchema(
        BaseRepositoryTest.newClasspathResourceReader("static/json-schema/GETpcrbatchJSONSchema.json"),
        BaseRepositoryTest.newClasspathResourceReader("realPcrbatchResponse-all.json"));
  }

  @Test
  public void getRegion_APIResponse_schemaValidates() throws IOException {
    JsonSchemaAssertions.assertJsonSchema(
        BaseRepositoryTest.newClasspathResourceReader("static/json-schema/pcrbatchJSONSchema.json"),
        BaseRepositoryTest.newClasspathResourceReader("realPcrbatchResponse.json"));
  }
}
