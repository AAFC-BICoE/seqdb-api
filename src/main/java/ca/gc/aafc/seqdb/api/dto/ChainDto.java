package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.Chain;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "chain")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(Chain.class)
public class ChainDto {

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String name;

  @JsonApiRelation
  private ChainTemplateDto chainTemplate;

}
