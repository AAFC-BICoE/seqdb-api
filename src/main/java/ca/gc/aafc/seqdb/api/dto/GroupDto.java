package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "group")
public class GroupDto {

  @JsonApiId
  private Integer groupId;
  
  private String groupName;
  
  private String description;
  
}
