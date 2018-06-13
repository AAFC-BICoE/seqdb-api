package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "pcrPrimer")
public class PcrPrimerDto {

  @JsonApiId
  private Integer pcrPrimerId;

  private String name;

  private PrimerType type;

  private String seq;

  private Integer lotNumber;

  @JsonApiRelation
  private RegionDto region;

}
