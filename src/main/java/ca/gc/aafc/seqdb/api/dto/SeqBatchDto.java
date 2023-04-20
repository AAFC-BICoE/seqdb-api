package ca.gc.aafc.seqdb.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.repository.meta.AttributeMetaInfoProvider;
import ca.gc.aafc.seqdb.api.entities.StorageRestriction;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonApiResource(type = SeqBatchDto.TYPENAME)
@RelatedEntity(SeqBatch.class)
@TypeName(SeqBatchDto.TYPENAME)
@EqualsAndHashCode(callSuper = false) //meta is not part of the data
public class SeqBatchDto extends AttributeMetaInfoProvider {

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

  @JsonApiExternalRelation(type = "person")
  @JsonApiRelation
  private List<ExternalRelationDto> experimenters = Collections.emptyList();

  @JsonApiRelation
  private ThermocyclerProfileDto thermocyclerProfile;

  @JsonApiRelation
  private RegionDto region;

  @JsonApiExternalRelation(type = "protocol")
  @JsonApiRelation
  private ExternalRelationDto protocol;

  @JsonApiExternalRelation(type = "storage-unit")
  @JsonApiRelation
  private ExternalRelationDto storageUnit;

  @JsonApiExternalRelation(type = "storage-unit-type")
  @JsonApiRelation
  private ExternalRelationDto storageUnitType;

  private StorageRestriction storageRestriction;

}
