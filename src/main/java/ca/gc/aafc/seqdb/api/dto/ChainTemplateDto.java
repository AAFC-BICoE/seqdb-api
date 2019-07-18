package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chainTemplate")
public class ChainTemplateDto {
  
  @JsonApiId
  private Integer chainTemplateId;
  
  private String name;
  
}
