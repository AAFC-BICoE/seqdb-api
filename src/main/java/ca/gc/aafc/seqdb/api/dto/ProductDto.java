package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import java.sql.Timestamp;

@Data
@JsonApiResource(type = "product")
public class ProductDto {
  
  @JsonApiId  
  private Integer productId;
  
  private String name;
  
  //Optional fields
  
  private String type;
  
  private String description;
  
  private String upc;  
  
  private Timestamp lastModified;  
  
  @JsonApiRelation
  private GroupDto group;
}
