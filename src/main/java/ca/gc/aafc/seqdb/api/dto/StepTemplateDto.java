package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.seqdb.entities.workflow.StepTemplate.StepResourceValue;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "stepTemplate")
@SuppressFBWarnings(value="EI_EXPOSE_REP")
public class StepTemplateDto {
  
  @JsonApiId
  private Integer stepTemplateId;
  
  private String name;
  
  private StepResourceValue[] inputs;
  
  private StepResourceValue[] outputs;
  
  private StepResourceValue[] supports;
  
}
