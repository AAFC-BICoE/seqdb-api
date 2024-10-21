package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@JsonApiResource(type = "molecular-analysis-result")
@RelatedEntity(MolecularAnalysisResult.class)
public class MolecularAnalysisResultDto {

  @JsonApiId
  private UUID uuid;

  private String group;

  private String createdBy;
  private OffsetDateTime createdOn;

  @JsonApiExternalRelation(type = "metadata")
  @JsonApiRelation
  private List<ExternalRelationDto> attachments = List.of();

}
