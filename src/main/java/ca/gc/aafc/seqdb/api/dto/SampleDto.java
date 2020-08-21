package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Sample;
import ca.gc.aafc.seqdb.api.entities.Sample.SampleType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "sample")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(Sample.class)
public class SampleDto {

  @JsonApiId
  private UUID uuid;
  
  // Required Fields

  private String name;

  private String version;

  // Optional Fields

  private SampleType sampleType;
  
  private String notes;

  private Timestamp lastModified;

  // Optional Relations

  @JsonApiRelation
  private ProductDto kit;

  @JsonApiRelation
  private ProtocolDto protocol;

}
