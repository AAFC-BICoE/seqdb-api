package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "step-resource")
@RelatedEntity(StepResource.class)
public class StepResourceDto {

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private StepResourceValue value;

  @JsonApiRelation
  private ChainStepTemplateDto chainStepTemplate;

  @JsonApiRelation
  private ChainDto chain;

  @JsonApiRelation
  private MolecularSampleDto molecularSample;

  @JsonApiRelation
  private LibraryPrepBatchDto libraryPrepBatch;

  @JsonApiRelation
  private LibraryPoolDto libraryPool;

}
