package ca.gc.aafc.seqdb.api.dto;

import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Region;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "region")
@RelatedEntity(Region.class)
public class RegionDto {

  @JsonApiId
  private UUID uuid;

  private String name;

  private String description;

  private String symbol;

}
