package ca.gc.aafc.seqdb.api.dto;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.javers.core.metamodel.annotation.TypeName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiRelation;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@JsonApiTypeForClass(SeqReactionDto.TYPENAME)
@RelatedEntity(SeqReaction.class)
@TypeName(SeqReactionDto.TYPENAME)
public class SeqReactionDto implements JsonApiResource {

  public static final String TYPENAME = "seq-reaction";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  // -- Relationships --
  @JsonIgnore
  @ShallowReference
  @JsonApiRelation
  private SeqBatchDto seqBatch;

  @JsonIgnore
  @ShallowReference
  @JsonApiRelation
  private PcrBatchItemDto pcrBatchItem;

  @JsonIgnore
  @ShallowReference
  @JsonApiRelation
  private PcrPrimerDto seqPrimer;

  @JsonIgnore
  @ShallowReference
  @JsonApiRelation
  private MolecularAnalysisRunItemDto molecularAnalysisRunItem;

  // -- External relationships --
  @JsonIgnore
  @JsonApiExternalRelation(type = "storage-unit-usage")
  private ExternalRelationDto storageUnitUsage;

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
