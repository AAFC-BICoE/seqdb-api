package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "reactionComponent")
public class ReactionComponentDto {

  @JsonApiId
  private Integer reactionComponentId;

  private String name;

  private String concentration;

  private String quantity;

  @JsonApiRelation(opposite = "reactionComponents")
  private ProtocolDto protocol;

}
