package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = SeqBatchDto.TYPENAME)
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(SeqBatch.class)
@TypeName(SeqBatchDto.TYPENAME)
public class SeqBatchDto {

  public static final String TYPENAME = "seq-batch";  

  @JsonApiId
  @Id
  @PropertyName("id")  
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;
  private String name;

  @JsonApiExternalRelation(type = "person")
  @JsonApiRelation
  private List<ExternalRelationDto> experimenters = Collections.emptyList();

  @JsonApiRelation
  private ThermocyclerProfileDto thermocyclerProfile;

  @JsonApiRelation
  private RegionDto region;

  @JsonApiExternalRelation(type = "protocol")
  @JsonApiRelation
  private ExternalRelationDto protocol;

}
