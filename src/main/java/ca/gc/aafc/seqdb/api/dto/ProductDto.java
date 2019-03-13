package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "product")
public class ProductDto {
  
  @JsonApiId  
  private Integer productId;
  
  private String name;
  
  private String type;
  
  private String description;
  
  @JsonApiRelation
  private GroupDto group;
}
