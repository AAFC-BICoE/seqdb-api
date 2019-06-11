package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Integration test for the region resource.
 *
 */
public class RegionJsonApiIT extends BaseJsonApiIntegrationTest {

  @Override
  protected String getResourceUnderTest() {
    return "region";
  }
  
  @Override
  protected String getGetOneSchemaFilename() {
    return "regionJSONSchema.json";
  }

  @Override
  protected String getGetManySchemaFilename() {
    return "GETregionJSONSchema.json";
  }
  
  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
        .put("name", "test region")
        .put("description", "test description")
        .put("symbol", "test symbol").build();
  }
  
  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
        .put("name", "updated name")
        .put("description", "updated description")
        .put("symbol", "updated symbol").build();
  }
}
