package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
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
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass(GenericMolecularAnalysisItemDto.TYPENAME)
@RelatedEntity(GenericMolecularAnalysisItem.class)
public class GenericMolecularAnalysisItemDto implements JsonApiResource {

  public static final String TYPENAME = "generic-molecular-analysis-item";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  @JsonIgnore
  private GenericMolecularAnalysisDto genericMolecularAnalysis;

  @JsonIgnore
  private MolecularAnalysisRunItemDto molecularAnalysisRunItem;

  // -- External relationships --
  @JsonApiExternalRelation(type = "material-sample")
  @JsonIgnore
  private ExternalRelationDto materialSample;

  @JsonApiExternalRelation(type = "storage-unit-usage")
  @JsonIgnore
  private ExternalRelationDto storageUnitUsage;

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
