package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatchItem;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiResource(type = MetagenomicsBatchItemDto.TYPENAME)
@RelatedEntity(MetagenomicsBatchItem.class)
public class MetagenomicsBatchItemDto {

  public static final String TYPENAME = "metagenomics-batch-item";

  @JsonApiId
  private UUID uuid;

  private String createdBy;

  private OffsetDateTime createdOn;

  @JsonApiRelation
  private MetagenomicsBatchDto metagenomicsBatch;

  @JsonApiRelation
  private PcrBatchItemDto pcrBatchItem;

  @JsonApiRelation
  private NgsIndexDto indexI5;

  @JsonApiRelation
  private NgsIndexDto indexI7;

}
