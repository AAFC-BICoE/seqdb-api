package ca.gc.aafc.seqdb.api.dto;

import java.util.List;

import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "pcrBatch")
public class PcrBatchDto {
  
  @JsonApiId
  private Integer pcrBatchId;
  
  private String name;
  
  private PcrBatchType type;
  
  private PcrBatchPlateSize plateSize;
  
  @JsonApiRelation(opposite = "pcrBatch")
  private List<PcrReactionDto> reactions;
  
  @JsonApiRelation
  private GroupDto group;
  
}
