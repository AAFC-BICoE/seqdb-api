package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "molecular-sample")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(MolecularSample.class)
public class MolecularSampleDto {

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;
  
  // Required Fields

  private String name;

  // Optional Relations

  @JsonApiRelation
  private ProductDto kit;

  @JsonApiRelation
  private ProtocolDto protocol;

  @JsonApiExternalRelation(type = "material-sample")
  @JsonApiRelation
  private ExternalRelationDto materialSample;

}
