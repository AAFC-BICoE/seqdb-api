package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;
import lombok.Data;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(LibraryPoolContentDto.TYPENAME)
@RelatedEntity(LibraryPoolContent.class)
public class LibraryPoolContentDto implements JsonApiResource {

  public static final String TYPENAME = "library-pool-content";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  //@JsonApiRelation
  private LibraryPoolDto libraryPool;
  
  //@JsonApiRelation
  private LibraryPrepBatchDto pooledLibraryPrepBatch;
  
  //@JsonApiRelation
  private LibraryPoolDto pooledLibraryPool;

  @Override
  public String getJsonApiType() {
    return TYPENAME;
  }

  @Override
  public UUID getJsonApiId() {
    return uuid;
  }
}
