package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatchItem;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.javers.core.metamodel.annotation.ShallowReference;

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
@JsonApiTypeForClass(MetagenomicsBatchItemDto.TYPENAME)
@RelatedEntity(MetagenomicsBatchItem.class)
public class MetagenomicsBatchItemDto implements JsonApiResource {

  public static final String TYPENAME = "metagenomics-batch-item";

  @JsonApiId
  private UUID uuid;

  private String createdBy;

  private OffsetDateTime createdOn;

  // -- Relationships --
  private MetagenomicsBatchDto metagenomicsBatch;

  private PcrBatchItemDto pcrBatchItem;

  @ShallowReference
  private MolecularAnalysisRunItemDto molecularAnalysisRunItem;

  private NgsIndexDto indexI5;

  private NgsIndexDto indexI7;

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
