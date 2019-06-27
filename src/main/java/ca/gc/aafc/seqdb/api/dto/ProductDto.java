package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import java.sql.Timestamp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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

  @SuppressFBWarnings(value="EI_EXPOSE_REP")  
  private Timestamp lastModified;  
  
  @JsonApiRelation
  private GroupDto group;
}
