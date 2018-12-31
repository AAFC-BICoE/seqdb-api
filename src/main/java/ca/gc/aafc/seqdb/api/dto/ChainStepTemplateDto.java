package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chainStepTemplate")
public class ChainStepTemplateDto {
  
  @JsonApiId
  private ChainStepTemplateDto chainTemplate;
  
  @JsonApiId
  private StepTemplateDto stepTemplate;
  
  private Integer stepNumber;
  
}
