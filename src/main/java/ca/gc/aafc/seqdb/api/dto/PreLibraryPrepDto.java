package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.seqdb.entities.PreLibraryPrep.PreLibraryPrepType;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "preLibraryPrep")
public class PreLibraryPrepDto {

  @JsonApiId
  private Integer preLibraryPrepId;

  private PreLibraryPrepType preLibraryPrepType;

  private Double inputAmount;

  private Double targetDpSize;

  private Double averageFragmentSize;

  private Double concentration;

  private String quality;

  private String notes;
  
  @JsonApiRelation
  private ProtocolDto protocol;
  
  @JsonApiRelation
  private ProductDto product;
  
}
