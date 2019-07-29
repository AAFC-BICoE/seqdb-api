package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.seqdb.entities.PreLibraryPrep.PreLibraryPrepType;

public class PreLibraryPrepJsonApiIT extends BaseJsonApiIntegrationTest {

  @Override
  protected String getResourceUnderTest() {
    return "preLibraryPrep";
  }
  
  @Override
  protected String getGetOneSchemaFilename() {
    return "getOnePreLibraryPrepSchema.json";
  }

  @Override
  protected String getGetManySchemaFilename() {
    return "getManyPreLibraryPrepSchema.json";
  }
  
  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
        .put("preLibraryPrepType", PreLibraryPrepType.SHEARING.toString())
        .put("notes", "test notes")
        .put("quality", "test quality").build();
  }
  
  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
        .put("preLibraryPrepType", PreLibraryPrepType.SIZE_SELECTION.toString())
        .put("notes", "updated notes")
        .put("quality", "updated quality").build();
  }

  @Override
  protected Map<String, Object> buildRelationshipMap() {
    // No relationships that can be tested.
    return null;
  }
  
}
