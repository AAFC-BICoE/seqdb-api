package ca.gc.aafc.seqdb.api.dto;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiRelation;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonApiTypeForClass(SeqBatchDto.TYPENAME)
@RelatedEntity(SeqBatch.class)
@TypeName(SeqBatchDto.TYPENAME)
@EqualsAndHashCode(callSuper = false) //meta is not part of the data
public class SeqBatchDto implements JsonApiResource {

  public static final String TYPENAME = "seq-batch";  

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  private String name;
  private String sequencingType;
  private Boolean isCompleted = false;
  private LocalDate reactionDate;

  // -- Relationships --
  @JsonIgnore
  @JsonApiRelation
  private ThermocyclerProfileDto thermocyclerProfile;

  @JsonIgnore
  @JsonApiRelation
  private RegionDto region;

  // -- External relationships --
  @JsonApiExternalRelation(type = "person")
  @JsonIgnore
  private List<ExternalRelationDto> experimenters = Collections.emptyList();

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
