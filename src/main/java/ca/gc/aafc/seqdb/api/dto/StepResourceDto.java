package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.seqdb.entities.workflow.StepTemplate.StepResourceValue;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "stepResource")
public class StepResourceDto {
  
  @JsonApiId
  private Integer stepResourceId;
  
  private StepResourceValue value;

  @JsonApiRelation
  private ChainStepTemplateDto chainStepTemplate; 
  
  @JsonApiRelation
  private ChainDto chain;
  
//  @JsonApiRelation
//  private SpecimenDto specimen;
//  
//  @JsonApiRelation
//  private SpecimenReplicateDto specimenReplicate;
//  
//  @JsonApiRelation
//  private MixedSpecimenDto mixedSpecimen;

  @JsonApiRelation
  private SampleDto sample;

//  @JsonApiRelation
//  private PcrBatchDto pcrBatch;
//  
//  @JsonApiRelation
//  private SeqBatchDto seqBatch;
//  
//  @JsonApiRelation
//  private SeqSubmissionDto seqSubmission;
//  
//  @JsonApiRelation
//  private ProductDto product;
  
  @JsonApiRelation
  private PcrPrimerDto primer;
  
//  @JsonApiRelation
//  private PcrProfileDto pcrProfile;
//  
//  @JsonApiRelation
//  private ProtocolDto protocol;
  
  @JsonApiRelation
  private PreLibraryPrepDto preLibraryPrep;
  
  @JsonApiRelation
  private LibraryPrepBatchDto libraryPrepBatch;

  @JsonApiRelation
  private LibraryPoolDto libraryPool;
  
}
