package ca.gc.aafc.seqdb.api.dto.pcr;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiRelation;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(PcrBatchDto.TYPENAME)
@RelatedEntity(PcrBatch.class)
@TypeName(PcrBatchDto.TYPENAME)
@EqualsAndHashCode(callSuper = false) //meta is not part of the data
public class PcrBatchDto implements JsonApiResource {

  public static final String TYPENAME = "pcr-batch";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;
  private String name;
  private String batchType;
  private Boolean isCompleted = false;

  private String thermocycler;
  private String objective;
  private String positiveControl;
  private String reactionVolume;

  private LocalDate reactionDate;

  @JsonIgnore
  @JsonApiRelation
  private PcrPrimerDto primerForward;

  @JsonIgnore
  @JsonApiRelation
  private PcrPrimerDto primerReverse;

  @JsonIgnore
  @JsonApiRelation
  private RegionDto region;

  @JsonIgnore
  @JsonApiRelation
  private ThermocyclerProfileDto thermocyclerProfile;

  @JsonIgnore
  @JsonApiExternalRelation(type = "person")
  private List<ExternalRelationDto> experimenters = List.of();

  @JsonIgnore
  @JsonApiExternalRelation(type = "metadata")
  private List<ExternalRelationDto> attachment = List.of();

  @JsonIgnore
  @JsonApiExternalRelation(type = "storage-unit")
  private ExternalRelationDto storageUnit;

  @JsonIgnore
  @JsonApiExternalRelation(type = "protocol")
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
