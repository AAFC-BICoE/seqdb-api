package ca.gc.aafc.seqdb.api.dto;

import java.sql.Date;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chain")
@SuppressFBWarnings(value="EI_EXPOSE_REP")
public class ChainDto {
  
  @JsonApiId
  private Integer chainId;
  
  private String name;
  
  private Date dateCreated;
  
  @JsonApiRelation
  private ChainTemplateDto chainTemplate;
  
  @JsonApiRelation
  private GroupDto group;
  
}
