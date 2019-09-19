package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "location")
public class LocationDto {
  
  @JsonApiId
  private Integer locationId;

  private Integer wellColumn;

  private String wellRow;

  private Timestamp dateMoved;

  private Timestamp lastModified;

  @JsonApiRelation
  private ContainerDto container;

  @JsonApiRelation
  private SampleDto sample;

  @JsonApiRelation
  private PcrPrimerDto pcrPrimer;

  // Unimplemented storable items.
  // @JsonApiRelation
  // private MixedSpecimenDto mixedSpecimen;
  //
  // @JsonApiRelation
  // private SpecimenReplicateDto specimenReplicate;
  //
  // @JsonApiRelation
  // private FragmentDto fragment;

}
