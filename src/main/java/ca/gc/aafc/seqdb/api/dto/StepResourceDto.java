package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "stepResource")
@RelatedEntity(StepResource.class)
public class StepResourceDto {

  @JsonApiId
  private Integer id;

  private StepResourceValue value;

  @JsonApiRelation
  private ChainStepTemplateDto chainStepTemplate;

  @JsonApiRelation
  private ChainDto chain;

  @JsonApiRelation
  private SampleDto sample;

  @JsonApiRelation
  private PcrPrimerDto primer;

  @JsonApiRelation
  private PreLibraryPrepDto preLibraryPrep;

  @JsonApiRelation
  private LibraryPrepBatchDto libraryPrepBatch;

  @JsonApiRelation
  private LibraryPoolDto libraryPool;

}
