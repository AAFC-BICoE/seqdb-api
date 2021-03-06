package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "library-pool-content")
@RelatedEntity(LibraryPoolContent.class)
public class LibraryPoolContentDto {
  
  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  @JsonApiRelation
  private LibraryPoolDto libraryPool;
  
  @JsonApiRelation
  private LibraryPrepBatchDto pooledLibraryPrepBatch;
  
  @JsonApiRelation
  private LibraryPoolDto pooledLibraryPool;

}
