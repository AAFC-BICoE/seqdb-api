package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.SeqBatch;

public class SeqBatchFactory implements TestableEntityFactory<SeqBatch> {

  @Override
  public SeqBatch getEntityInstance() {
    return newSeqBatch().build();
  }
    
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @return Pre-configured builder with all mandatory fields set
   */
  public static SeqBatch.SeqBatchBuilder newSeqBatch() {
    
    return SeqBatch.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .name(TestableEntityFactory.generateRandomName(10))
      .group("dina");
    
  }  
  
}

