package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.SeqSubmission;

public class SeqSubmissionFactory implements TestableEntityFactory<SeqSubmission> {

  @Override
  public SeqSubmission getEntityInstance() {
    return newSeqSubmission().build();
  }
    
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @return Pre-configured builder with all mandatory fields set
   */
  public static SeqSubmission.SeqSubmissionBuilder newSeqSubmission() {
    
    return SeqSubmission.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .group("dina")
      .name("submission");
    
  }  
  
}
