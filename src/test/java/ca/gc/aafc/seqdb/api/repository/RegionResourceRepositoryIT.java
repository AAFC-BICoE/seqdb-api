package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;

import org.junit.Test;

public class RegionResourceRepositoryIT {

  @Test
  public void findAllRegions_Validation() throws IOException {

    JsonSchemaAssertions.assertJsonSchema("json-schema/GETregionJSONSchema.json",
        "realRegionResponse-all.json");
    JsonSchemaAssertions.assertJsonSchema("json-schema/regionJSONSchema.json",
        "realRegionResponse.json");

  }
}
