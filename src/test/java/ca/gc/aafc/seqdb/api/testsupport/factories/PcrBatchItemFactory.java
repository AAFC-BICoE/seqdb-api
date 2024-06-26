package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;

public class PcrBatchItemFactory implements TestableEntityFactory<PcrBatchItem> {

  @Override
  public PcrBatchItem getEntityInstance() {
    return newPcrBatchItem().build();
  }
    
  /**
   * Static method that can be called to return a configured builder that can be further customized
   * to return the actual entity object, call the .build() method on a builder.
   * @return Pre-configured builder with all mandatory fields set
   */
  public static PcrBatchItem.PcrBatchItemBuilder newPcrBatchItem() {
    
    return PcrBatchItem.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .group("dina");
  }  
  
}
