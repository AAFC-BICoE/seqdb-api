package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiField;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import io.crnk.core.resource.annotations.PatchStrategy;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = GenericMolecularAnalysisDto.TYPENAME)
@RelatedEntity(GenericMolecularAnalysis.class)
public class GenericMolecularAnalysisDto {

  public static final String TYPENAME = "generic-molecular-analysis";

  @JsonApiId
  private UUID uuid;

  private String createdBy;

  private OffsetDateTime createdOn;
  private String group;

  private String name;

  private String analysisType;

  @JsonApiExternalRelation(type = "protocol")
  @JsonApiRelation
  private ExternalRelationDto protocol;

  /**
   * Map of Managed attribute key to value object.
   */
  @JsonApiField(patchStrategy = PatchStrategy.SET)
  @Builder.Default
  private Map<String, String> managedAttributes = Map.of();

}
