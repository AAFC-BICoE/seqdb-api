package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "pcrReaction")
public class PcrReactionDto {

  @JsonApiId
  private Integer pcrReactionId;

  private String pcrName;

  @JsonApiRelation(opposite = "reactions")
  private PcrBatchDto pcrBatch;

}
