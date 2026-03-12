package ca.gc.aafc.seqdb.api.dto;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;
import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Region;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(RegionDto.TYPENAME)
@RelatedEntity(Region.class)
@TypeName(RegionDto.TYPENAME)
public class RegionDto implements JsonApiResource {

  public static final String TYPENAME = "region";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;

  private String name;

  private String description;

  private String symbol;

  private String applicableOrganisms;

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
