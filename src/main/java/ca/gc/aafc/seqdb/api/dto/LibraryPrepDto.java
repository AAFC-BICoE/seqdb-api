package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "libraryPrep")
public class LibraryPrepDto {
  
  @JsonApiId
  private Integer libraryPrepId;

  private Double inputNg;

  private String quality;

  private String size;

  private Integer wellColumn;

  private String wellRow;

  @JsonApiRelation
  private LibraryPrepBatchDto libraryPrepBatch;
  
  @JsonApiRelation
  private SampleDto sample;

  @JsonApiRelation
  private NgsIndexDto indexI5;

  @JsonApiRelation
  private NgsIndexDto indexI7;

}
