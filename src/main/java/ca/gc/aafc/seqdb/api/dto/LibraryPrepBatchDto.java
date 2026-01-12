package ca.gc.aafc.seqdb.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(LibraryPrepBatchDto.TYPENAME)
@RelatedEntity(LibraryPrepBatch.class)
public class LibraryPrepBatchDto implements JsonApiResource {

  public static final String TYPENAME = "library-prep-batch";

  @JsonApiId
  private UUID uuid;

  private String name;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  private Double totalLibraryYieldNm;

  private String notes;

  private String cleanUpNotes;

  private String yieldNotes;
  
  private LocalDate dateUsed;

  // -- Relationships --

  @JsonIgnore
  private ProductDto product;

  @JsonIgnore
  private ThermocyclerProfileDto thermocyclerProfile;

  @JsonIgnore
  private IndexSetDto indexSet;

  @JsonIgnore
  private List<LibraryPrepDto> libraryPreps;

  // -- External relationships --
  @JsonApiExternalRelation(type = "protocol")
  @JsonIgnore
  private ExternalRelationDto protocol;

  @JsonApiExternalRelation(type = "storage-unit")
  @JsonIgnore
  private ExternalRelationDto storageUnit;

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
