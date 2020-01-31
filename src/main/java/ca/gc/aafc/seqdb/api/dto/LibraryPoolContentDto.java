package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "libraryPoolContent")
public class LibraryPoolContentDto {
  
  @JsonApiId
  private Integer libraryPoolContentId;
  
  @JsonApiRelation
  private LibraryPoolDto libraryPool;
  
  @JsonApiRelation
  private LibraryPrepBatchDto pooledLibraryPrepBatch;
  
  @JsonApiRelation
  private LibraryPoolDto pooledLibraryPool;

}
