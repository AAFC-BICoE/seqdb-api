package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@JsonApiResource(type = "molecular-analysis-run-item")
@RelatedEntity(MolecularAnalysisRunItem.class)
public class MolecularAnalysisRunItemDto {

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  @JsonApiRelation
  private MolecularAnalysisRunDto run;

  @JsonApiRelation
  private MolecularAnalysisResultDto result;

}
