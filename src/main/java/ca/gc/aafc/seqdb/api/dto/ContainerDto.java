package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "container")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class ContainerDto {

  @JsonApiId
  private Integer containerId;

  private String containerNumber;

  private Boolean fillByRow;

  private Timestamp lastModified;

  @JsonApiRelation
  private ContainerTypeDto containerType;

  @JsonApiRelation(opposite = "container")
  private List<LocationDto> locations;

  @JsonApiRelation
  private GroupDto group;

}
