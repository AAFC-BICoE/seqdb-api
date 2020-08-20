package ca.gc.aafc.seqdb.api.dto;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Region;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = RegionDto.TYPENAME)
@RelatedEntity(Region.class)
@TypeName(RegionDto.TYPENAME)
public class RegionDto {

  public static final String TYPENAME = "region";

  @JsonApiId
  @Id
  @PropertyName("id")
  private Integer regionId;

  private String name;

  private String description;

  private String symbol;

}
