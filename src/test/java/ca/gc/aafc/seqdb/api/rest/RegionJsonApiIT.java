package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import org.junit.platform.commons.annotation.Testable;

import ca.gc.aafc.seqdb.api.entities.Region;
import ca.gc.aafc.seqdb.api.testsupport.factories.RegionFactory;

/**
 * Integration test for the region resource.
 *
 */
@Testable
public class RegionJsonApiIT extends BaseJsonApiIntegrationTest {

  private int nameIncrementor = 1;

  @Override
  protected String getResourceUnderTest() {
    return "region";
  }
  
  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    
    Region region = RegionFactory.newRegion()
    .name("test region" + (nameIncrementor++))
    .description("test description")
    .symbol("test symbol").build();
    
    Map<String, Object> map = toAttributeMap(region);
    
    return map;
  }
  
  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
  Region region = RegionFactory.newRegion()
    .name("updated name")
    .description("updated description")
    .symbol("updated symbol").build();
    
    Map<String, Object> map = toAttributeMap(region);
    
    return map;
  }

}
