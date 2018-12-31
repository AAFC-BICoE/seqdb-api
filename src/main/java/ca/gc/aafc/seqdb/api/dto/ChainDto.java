package ca.gc.aafc.seqdb.api.dto;

import java.sql.Date;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chain")
public class ChainDto {
  
  @JsonApiId
  private Integer chainId;
  
  private String name;
  
  private Date dateCreated;
  
  @JsonApiRelation
  private ChainStepTemplateDto chainTemplate;
  
  @JsonApiRelation
  private GroupDto group;
  
}
