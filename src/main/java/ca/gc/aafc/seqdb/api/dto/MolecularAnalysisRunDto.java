package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@JsonApiResource(type = "molecular-analysis-run")
@RelatedEntity(MolecularAnalysisRun.class)
public class MolecularAnalysisRunDto {

  @JsonApiId
  private UUID uuid;

  private String createdBy;

  private OffsetDateTime createdOn;
  private String group;

  private String name;

}
