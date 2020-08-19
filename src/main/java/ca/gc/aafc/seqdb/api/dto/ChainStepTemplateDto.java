package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainStepTemplate;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chainStepTemplate")
@RelatedEntity(ChainStepTemplate.class)
public class ChainStepTemplateDto {
  
  @JsonApiId
  private Integer chainStepTemplateId;
  
  @JsonApiRelation
  private ChainTemplateDto chainTemplate;
  
  @JsonApiRelation
  private StepTemplateDto stepTemplate;
  
  private Integer stepNumber;
  
}
