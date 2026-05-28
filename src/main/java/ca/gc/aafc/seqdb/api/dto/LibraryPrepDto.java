package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(LibraryPrepDto.TYPENAME)
@RelatedEntity(LibraryPrep.class)
public class LibraryPrepDto implements JsonApiResource {

  public static final String TYPENAME = "library-prep";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;
  
  private Double inputNg;
  private String quality;
  private String size;

  // -- Relationships --
  @JsonIgnore
  private NgsIndexDto indexI5;

  @JsonIgnore
  private NgsIndexDto indexI7;

  @JsonIgnore
  private LibraryPrepBatchDto libraryPrepBatch;

  // -- External relationships --
  @JsonApiExternalRelation(type = "storage-unit-usage")
  @JsonIgnore
  private ExternalRelationDto storageUnitUsage;

  @JsonApiExternalRelation(type = "material-sample")
  @JsonIgnore
  private ExternalRelationDto materialSample;

  @Override
  @JsonIgnore
  public String getJsonApiType() {
    return TYPENAME;
  }

  @Override
  @JsonIgnore
  public UUID getJsonApiId() {
    return uuid;
  }
}
