package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import ca.gc.aafc.seqdb.api.entities.Protocol.ProtocolType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "protocol")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(Protocol.class)
public class ProtocolDto {

  @JsonApiId
  private UUID uuid;

  private ProtocolType type;

  private String name;

  private String version;

  private String description;

  private String steps;

  private String notes;

  private String reference;

  private String equipment;

  private String forwardPrimerConcentration;

  private String reversePrimerConcentration;

  private String reactionMixVolume;

  private String reactionMixVolumePerTube;

  private Timestamp lastModified;

  @JsonApiRelation
  private ProductDto kit;

}
