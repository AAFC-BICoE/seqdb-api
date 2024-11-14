package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = GenericMolecularAnalysisItemDto.TYPENAME)
@RelatedEntity(GenericMolecularAnalysisItem.class)
public class GenericMolecularAnalysisItemDto {

  public static final String TYPENAME = "generic-molecular-analysis-item";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  @JsonApiExternalRelation(type = "material-sample")
  @JsonApiRelation
  private ExternalRelationDto materialSample;

  @JsonApiExternalRelation(type = "storage-unit-usage")
  @JsonApiRelation
  private ExternalRelationDto storageUnitUsage;

  @JsonApiRelation
  private GenericMolecularAnalysisDto genericMolecularAnalysis;

  @JsonApiRelation
  private MolecularAnalysisRunItemDto molecularAnalysisRunItem;

}
