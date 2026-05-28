package ca.gc.aafc.seqdb.api.dto.pcr;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiRelation;
import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@JsonApiTypeForClass(PcrBatchItemDto.TYPENAME)
@RelatedEntity(PcrBatchItem.class)
@TypeName(PcrBatchItemDto.TYPENAME)
public class PcrBatchItemDto implements JsonApiResource {

  public static final String TYPENAME = "pcr-batch-item";

  @JsonApiId
  @Id
  @PropertyName("id")  
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;
  private String result;

  @JsonIgnore
  @JsonApiRelation
  private PcrBatchDto pcrBatch;

  @JsonIgnore
  @JsonApiExternalRelation(type = "material-sample")
  private ExternalRelationDto materialSample;

  @JsonIgnore
  @JsonApiExternalRelation(type = "storage-unit-usage")
  private ExternalRelationDto storageUnitUsage;

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
