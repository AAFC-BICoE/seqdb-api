package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Product;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(ProductDto.TYPENAME)
@RelatedEntity(Product.class)
public class ProductDto implements JsonApiResource {

  public static final String TYPENAME = "product";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;
  
  private String name;

  // Optional fields

  private String type;

  private String description;

  private String upc;

  private Timestamp lastModified;

  @Override
  @JsonIgnore
  public String getJsonApiType() {
    return TYPENAME;
  }

  @Override
  @JsonIgnore
  public UUID getJsonApiId() {
    return uuid;
  }
}
