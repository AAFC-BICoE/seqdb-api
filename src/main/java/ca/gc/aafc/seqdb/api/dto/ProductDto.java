package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Product;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "product")
@SuppressFBWarnings(value="EI_EXPOSE_REP")
@RelatedEntity(Product.class)
public class ProductDto {
  
  @JsonApiId
  private UUID uuid;
  
  private String name;
  
  //Optional fields
  
  private String type;
  
  private String description;
  
  private String upc;  

  private Timestamp lastModified;  
  
}
