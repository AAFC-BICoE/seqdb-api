package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "container")
public class ContainerDto {

  @JsonApiId
  private Integer containerId;

  private String containerNumber;

  private Boolean fillByRow;

  private Timestamp lastModified;

  @JsonApiRelation
  private ContainerTypeDto containerType;

  @JsonApiRelation
  private GroupDto group;

}
