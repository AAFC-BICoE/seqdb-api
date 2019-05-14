package ca.gc.aafc.seqdb.api.dto;

 import java.util.List;

 import ca.gc.aafc.seqdb.entities.Protocol.ProtocolType;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "protocol")
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

 	@JsonApiRelation(opposite="protocol")
	private List <ReactionComponentDto> reactionComponents;

 	@JsonApiRelation
	private GroupDto group;
}