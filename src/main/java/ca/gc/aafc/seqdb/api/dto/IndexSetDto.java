package ca.gc.aafc.seqdb.api.dto;

import java.util.List;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "indexSet")
public class IndexSetDto {

  @JsonApiId
  private Integer indexSetId;

  private String name;

  private String forwardAdapter;

  private String reverseAdapter;

  @JsonApiRelation(opposite = "indexSet")
  private List<NgsIndexDto> ngsIndexes;

}
