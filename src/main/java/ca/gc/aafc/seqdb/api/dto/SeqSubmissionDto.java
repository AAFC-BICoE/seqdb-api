package ca.gc.aafc.seqdb.api.dto;

import org.javers.core.metamodel.annotation.ShallowReference;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiRelation;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.SeqSubmission;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Data
@JsonApiTypeForClass(SeqSubmissionDto.TYPENAME)
@RelatedEntity(SeqSubmission.class)
@TypeName(SeqSubmissionDto.TYPENAME)
public class SeqSubmissionDto implements JsonApiResource {

  public static final String TYPENAME = "seq-submission";

  @JsonApiId
  private UUID uuid;

  private String group;
  private String name;
  private String createdBy;
  private OffsetDateTime createdOn;

  @JsonIgnore
  @ShallowReference
  @JsonApiRelation
  private SequencingFacilityDto sequencingFacility;

  @JsonIgnore
  @JsonApiExternalRelation(type = "person")
  private ExternalRelationDto submittedBy;

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
