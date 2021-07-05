package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.PcrReaction;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = PcrReactionDto.TYPENAME)
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(PcrReaction.class)
@TypeName(PcrReactionDto.TYPENAME)
public class PcrReactionDto {

  public static final String TYPENAME = "pcr-reaction";
  
  @JsonApiId
  @Id
  @PropertyName("id")  
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  @JsonApiExternalRelation(type = "metadata")
  @JsonApiRelation
  private List<ExternalRelationDto> attachment = Collections.emptyList();

  @JsonApiRelation
  private PcrBatchDto pcrBatch;

  @JsonApiRelation
  private MolecularSampleDto sample;
}
