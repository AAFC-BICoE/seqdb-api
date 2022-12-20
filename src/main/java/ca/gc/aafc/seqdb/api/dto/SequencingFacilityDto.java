package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.SequencingFacility;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonApiResource(type = SequencingFacilityDto.TYPENAME)
@RelatedEntity(SequencingFacility.class)
@TypeName(SequencingFacilityDto.TYPENAME)
public class SequencingFacilityDto {

  public static final String TYPENAME = "sequencing-facility";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;
  private String name;

  private List<SequencingFacility.ContactRole> contacts = List.of();
  private SequencingFacility.Address shippingAddress;

}
