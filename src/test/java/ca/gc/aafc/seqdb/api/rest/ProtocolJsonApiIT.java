package ca.gc.aafc.seqdb.api.rest;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.seqdb.api.testsupport.factories.ProtocolFactory;

/**
 * Integration test for the region resource.
 *
 */
public class ProtocolJsonApiIT extends BaseJsonApiIntegrationTest {
  
  @Override
  protected String getResourceUnderTest() {
    return "protocol";
  }

  @Override
  protected String getGetOneSchemaFilename() {
    return "getOneProtocolSchema.json";
  }

  @Override
  protected String getGetManySchemaFilename() {
    return "getManyProtocolSchema.json";
  }

  @Override
  protected Map<String, Object> buildCreateAttributeMap() {
    
    return new ImmutableMap.Builder<String, Object>()
        .put("name", ProtocolFactory.newProtocol().build().getName())
        .put("type", "SEQ_REACTION")
        .put("version", "A")
        .put("description", "test description")
        .put("steps", "14")
        .put("notes", "test notes")
        .put("reference", "test reference")
        .put("equipment", "test equipment")
        .put("forwardPrimerConcentration", "10")
        .put("reversePrimerConcentration", "2")
        .put("reactionMixVolume", "4.0mL")
        .put("reactionMixVolumePerTube", "10dL")
        .build();
  }

  @Override
  protected Map<String, Object> buildRelationshipMap() {
    return ImmutableMap.of();
  }

  @Override
  protected Map<String, Object> buildUpdateAttributeMap() {
    return new ImmutableMap.Builder<String, Object>()
        .put("name", ProtocolFactory.newProtocol().build().getName())
        .put("type", "COLLECTION_EVENT")
        .put("version", "B")
        .put("description", "new test description")
        .put("steps", "15")
        .put("notes", "new test notes")
        .put("reference", "new test reference")
        .put("equipment", "new test equipment")
        .put("forwardPrimerConcentration", "11")
        .put("reversePrimerConcentration", "3")
        .put("reactionMixVolume", "4.5mL")
        .put("reactionMixVolumePerTube", "100dL")
        .build();
  }
}
