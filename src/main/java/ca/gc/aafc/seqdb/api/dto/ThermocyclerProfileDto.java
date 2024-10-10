package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = ThermocyclerProfileDto.TYPENAME)
@RelatedEntity(ThermocyclerProfile.class)
public class ThermocyclerProfileDto {

  public static final String TYPENAME = "thermocycler-profile";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;

  private String name;

  private String application;

  private String cycles;

  private Timestamp lastModified;

  private List<String> steps;

  @JsonApiRelation
  private RegionDto region;

}
