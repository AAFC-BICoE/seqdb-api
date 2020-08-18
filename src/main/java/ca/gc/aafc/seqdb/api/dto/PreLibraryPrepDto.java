package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepType;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "preLibraryPrep")
@RelatedEntity(PreLibraryPrep.class)
public class PreLibraryPrepDto {

  @JsonApiId
  private Integer id;

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
