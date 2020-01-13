package ca.gc.aafc.seqdb.api.dto;

import java.time.LocalDate;
import java.util.List;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "libraryPrepBatch")
public class LibraryPrepBatchDto {

  @JsonApiId
  private Integer libraryPrepBatchId;

  private String name;

  private Double totalLibraryYieldNm;

  private String notes;

  private String cleanUpNotes;

  private String yieldNotes;
  
  private LocalDate dateUsed;

  @JsonApiRelation
  private ProductDto product;

  @JsonApiRelation
  private ProtocolDto protocol;

  @JsonApiRelation
  private ContainerTypeDto containerType;

  @JsonApiRelation
  private ThermocyclerProfileDto thermocyclerProfile;

  @JsonApiRelation
  private IndexSetDto indexSet;

  @JsonApiRelation(opposite = "libraryPrepBatch")
  private List<LibraryPrepDto> libraryPreps;

}
