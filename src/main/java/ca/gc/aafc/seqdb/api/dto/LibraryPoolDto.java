package ca.gc.aafc.seqdb.api.dto;

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

  @JsonApiRelation(opposite = "libraryPool")
  private List<LibraryPoolContentDto> contents;

}
