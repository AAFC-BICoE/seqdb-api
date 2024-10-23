package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.javers.core.metamodel.annotation.TypeName;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@JsonApiResource(type = SeqReactionDto.TYPENAME)
@RelatedEntity(SeqReaction.class)
@TypeName(SeqReactionDto.TYPENAME)
public class SeqReactionDto {

  public static final String TYPENAME = "seq-reaction";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  @ShallowReference
  @JsonApiRelation
  private SeqBatchDto seqBatch;

  @ShallowReference
  @JsonApiRelation
  private PcrBatchItemDto pcrBatchItem;

  @ShallowReference
  @JsonApiRelation
  private PcrPrimerDto seqPrimer;

  @ShallowReference
  @JsonApiRelation
  private MolecularAnalysisRunItemDto molecularAnalysisRunItem;

  @JsonApiExternalRelation(type = "storage-unit-usage")
  @JsonApiRelation
  private ExternalRelationDto storageUnitUsage;

}
