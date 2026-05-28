package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass(MolecularAnalysisRunDto.TYPENAME)
@RelatedEntity(MolecularAnalysisRun.class)
public class MolecularAnalysisRunDto implements JsonApiResource {

  public static final String TYPENAME = "molecular-analysis-run";

  @JsonApiId
  private UUID uuid;

  private String createdBy;

  private OffsetDateTime createdOn;
  private String group;

  private String name;

  @JsonApiExternalRelation(type = "metadata")
  @JsonIgnore
  private List<ExternalRelationDto> attachments = List.of();

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
