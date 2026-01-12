package ca.gc.aafc.seqdb.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import lombok.Data;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(LibraryPoolDto.TYPENAME)
@RelatedEntity(LibraryPool.class)
public class LibraryPoolDto implements JsonApiResource {

  public static final String TYPENAME = "library-pool";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  private String name;
  
  private LocalDate dateUsed;
  
  private String notes;

  //@JsonApiRelation
  private List<LibraryPoolContentDto> contents;

  @Override
  public String getJsonApiType() {
    return TYPENAME;
  }

  @Override
  public UUID getJsonApiId() {
    return uuid;
  }
}
