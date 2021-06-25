package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.PcrBatch;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = PcrBatchDto.TYPENAME)
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(PcrBatch.class)
@TypeName(PcrBatchDto.TYPENAME)
public class PcrBatchDto {

  public static final String TYPENAME = "pcrBatch";  

  @JsonApiId
  @Id
  @PropertyName("id")  
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;
  
  private List<UUID> experimenters;

  @JsonApiRelation
  private PcrPrimerDto primerForward;

  @JsonApiRelation
  private PcrPrimerDto primerReverse;

  @JsonApiRelation
  private RegionDto region;

}
