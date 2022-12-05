package ca.gc.aafc.seqdb.api.testsupport.factories;

import ca.gc.aafc.seqdb.api.entities.SeqReaction;

import java.util.UUID;

public class SeqRegionFactory implements TestableEntityFactory<SeqReaction> {

  @Override
  public SeqReaction getEntityInstance() {
    return newSeqReaction().build();
  }
    
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @return Pre-configured builder with all mandatory fields set
   */
  public static SeqReaction.SeqReactionBuilder newSeqReaction() {
    return SeqReaction.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .group("dina");
  }  
  
}

