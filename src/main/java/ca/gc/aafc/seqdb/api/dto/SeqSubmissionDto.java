package ca.gc.aafc.seqdb.api.dto;

import org.javers.core.metamodel.annotation.ShallowReference;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.SeqSubmission;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@JsonApiResource(type = SeqSubmissionDto.TYPENAME)
@RelatedEntity(SeqSubmission.class)
@TypeName(SeqSubmissionDto.TYPENAME)
public class SeqSubmissionDto {

  public static final String TYPENAME = "seq-submission";

  @JsonApiId
  private UUID uuid;

  private String group;
  private String name;
  private String createdBy;
  private OffsetDateTime createdOn;

  @ShallowReference
  @JsonApiRelation
  private SeqBatchDto seqBatch;

  @ShallowReference
  @JsonApiRelation
  private SequencingFacilityDto sequencingFacility;

  @JsonApiExternalRelation(type = "person")
  @JsonApiRelation
  private ExternalRelationDto submittedBy;

}
