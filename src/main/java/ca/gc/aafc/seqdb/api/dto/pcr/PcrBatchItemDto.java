package ca.gc.aafc.seqdb.api.dto.pcr;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = PcrBatchItemDto.TYPENAME)
@RelatedEntity(PcrBatchItem.class)
@TypeName(PcrBatchItemDto.TYPENAME)
public class PcrBatchItemDto {

  public static final String TYPENAME = "pcr-batch-item";

  @JsonApiId
  @Id
  @PropertyName("id")  
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  @JsonApiRelation
  private PcrBatchDto pcrBatch;

  @JsonApiExternalRelation(type = "material-sample")
  @JsonApiRelation
  private ExternalRelationDto materialSample;

  @JsonApiExternalRelation(type = "storage-unit-usage")
  @JsonApiRelation
  private ExternalRelationDto storageUnitUsage;

  private String result;

}
