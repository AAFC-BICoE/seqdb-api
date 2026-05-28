package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass(GenericMolecularAnalysisDto.TYPENAME)
@RelatedEntity(GenericMolecularAnalysis.class)
public class GenericMolecularAnalysisDto implements JsonApiResource {

  public static final String TYPENAME = "generic-molecular-analysis";

  @JsonApiId
  private UUID uuid;

  private String createdBy;

  private OffsetDateTime createdOn;
  private String group;

  private String name;

  private String analysisType;

  /**
   * Map of Managed attribute key to value object.
   */
  @Builder.Default
  private Map<String, String> managedAttributes = Map.of();

  // -- External relationships --
  @JsonApiExternalRelation(type = "protocol")
  @JsonIgnore
  private ExternalRelationDto protocol;

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
