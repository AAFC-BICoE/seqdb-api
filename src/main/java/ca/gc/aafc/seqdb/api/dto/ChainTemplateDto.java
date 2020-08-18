package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainTemplate;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chainTemplate")
@RelatedEntity(ChainTemplate.class)
public class ChainTemplateDto {
  
  @JsonApiId
  private Integer id;
  
  private String name;
  
}
