package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiRelation;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepType;
import lombok.Data;

import org.javers.core.metamodel.annotation.ShallowReference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(PreLibraryPrepDto.TYPENAME)
@RelatedEntity(PreLibraryPrep.class)
public class PreLibraryPrepDto implements JsonApiResource {

  public static final String TYPENAME = "pre-library-prep";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  private PreLibraryPrepType preLibraryPrepType;

  private Double inputAmount;

  private Double targetBpSize;

  private Double averageFragmentSize;

  private Double concentration;

  private String quality;

  private String notes;

  // -- Relationships --
  @JsonIgnore
  @ShallowReference
  @JsonApiRelation
  private LibraryPrepDto libraryPrep;

  @JsonApiRelation
  @JsonIgnore
  private ProductDto product;

  // -- External relationships --
  @JsonApiExternalRelation(type = "protocol")
  @JsonIgnore
  private ExternalRelationDto protocol;

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
