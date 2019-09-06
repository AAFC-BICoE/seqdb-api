package ca.gc.aafc.seqdb.api.dto;

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

  private Double totalLibraryYieldNm;

  private String notes;

  private String cleanUpNotes;

  private String yieldnotes;

  @JsonApiRelation
  private ProductDto product;

  @JsonApiRelation
  private ProtocolDto protocol;

  @JsonApiRelation(opposite = "libraryPrepBatch")
  private List<LibraryPrepDto> libraryPreps;

}
