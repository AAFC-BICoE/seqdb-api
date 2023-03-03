package ca.gc.aafc.seqdb.api.dto.pcr;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.seqdb.api.entities.StorageRestriction;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = PcrBatchDto.TYPENAME)
@RelatedEntity(PcrBatch.class)
@TypeName(PcrBatchDto.TYPENAME)
public class PcrBatchDto {

  public static final String TYPENAME = "pcr-batch";  

  @JsonApiId
  @Id
  @PropertyName("id")  
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;
  private String name;

  @JsonApiRelation
  private PcrPrimerDto primerForward;

  @JsonApiRelation
  private PcrPrimerDto primerReverse;

  @JsonApiRelation
  private RegionDto region;

  @JsonApiRelation
  private ThermocyclerProfileDto thermocyclerProfile;

  private String thermocycler;
  private String objective;
  private String positiveControl;
  private String reactionVolume;

  private LocalDate reactionDate;

  @JsonApiExternalRelation(type = "person")
  @JsonApiRelation
  private List<ExternalRelationDto> experimenters = List.of();

  @JsonApiExternalRelation(type = "metadata")
  @JsonApiRelation
  private List<ExternalRelationDto> attachment = List.of();

  @JsonApiExternalRelation(type = "storage-unit")
  @JsonApiRelation
  private ExternalRelationDto storageUnit;

  @JsonApiExternalRelation(type = "storage-unit-type")
  @JsonApiRelation
  private ExternalRelationDto storageUnitType;

  @JsonApiExternalRelation(type = "protocol")
  @JsonApiRelation
  private ExternalRelationDto protocol;

  private StorageRestriction storageRestriction;

}
