package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

import java.time.OffsetDateTime;
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
@JsonApiTypeForClass(MolecularAnalysisRunItemDto.TYPENAME)
@RelatedEntity(MolecularAnalysisRunItem.class)
public class MolecularAnalysisRunItemDto implements JsonApiResource {

  public static final String TYPENAME = "molecular-analysis-run-item";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String usageType;

  private String name;

  private MolecularAnalysisRunDto run;

  private MolecularAnalysisResultDto result;

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
