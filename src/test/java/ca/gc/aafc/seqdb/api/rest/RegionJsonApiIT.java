package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import ca.gc.aafc.seqdb.entities.Region;
import ca.gc.aafc.seqdb.testsupport.factories.RegionFactory;

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
    return "getOneRegionSchema.json";
  }

  @Override
  protected String getGetManySchemaFilename() {
    return "getManyRegionSchema.json";
  }
  
  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    
    Region region = RegionFactory.newRegion()
    .name("test region")
    .description("test description")
    .symbol("test symbol").build();
    
    Map<String, Object> map = toAttributeMap(region);
    // we should not be able to provide those fields but the API currently allows it
    map.remove("lft");
    map.remove("rgt");
    
    return map;
  }
  
  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    Region region = RegionFactory.newRegion()
    .name("updated name")
    .description("updated description")
    .symbol("updated symbol").build();
    
    Map<String, Object> map = toAttributeMap(region);
    // we should not be able to provide those fields but the API currently allows it
    map.remove("lft");
    map.remove("rgt");
    
    return map;
  }

}
