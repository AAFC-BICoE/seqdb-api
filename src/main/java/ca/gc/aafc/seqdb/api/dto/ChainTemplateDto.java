package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.ChainTemplate;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chain-template")
@RelatedEntity(ChainTemplate.class)
public class ChainTemplateDto {
  
  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;
  
  private String name;
  
}
