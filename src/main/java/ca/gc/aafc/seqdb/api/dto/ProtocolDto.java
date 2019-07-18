package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.seqdb.entities.Protocol.ProtocolType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
@JsonApiResource(type = "protocol")
@SuppressFBWarnings(value="EI_EXPOSE_REP")
public class ProtocolDto {

  @JsonApiId
  private Integer protocolId;

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

  @JsonApiRelation(opposite = "protocol")
  private List<ReactionComponentDto> reactionComponents;
 
  @JsonApiRelation
  private ProductDto kit;

  @JsonApiRelation
  private GroupDto group;
}
