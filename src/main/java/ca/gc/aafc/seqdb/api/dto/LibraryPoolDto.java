package ca.gc.aafc.seqdb.api.dto;

import java.time.LocalDate;
import java.util.List;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "libraryPool")
public class LibraryPoolDto {

  @JsonApiId
  private Integer libraryPoolId;

  private String name;
  
  private LocalDate dateUsed;
  
  private String notes;

  @JsonApiRelation(opposite = "libraryPool")
  private List<LibraryPoolContentDto> contents;

}
