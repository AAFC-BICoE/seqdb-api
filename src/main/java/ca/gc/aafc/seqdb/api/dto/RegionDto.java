package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "region")
public class RegionDto {

  @JsonApiId
  private Integer regionId;

  private String name;

  private String description;

  private String symbol;

}
