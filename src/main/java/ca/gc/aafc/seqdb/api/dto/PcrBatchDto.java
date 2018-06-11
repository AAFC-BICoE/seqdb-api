package ca.gc.aafc.seqdb.api.dto;

import java.util.List;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@JsonApiResource(type = "pcrBatch")
public class PcrBatchDto {

  @RequiredArgsConstructor
  public static enum PcrBatchType {
    
    SANGER("Sanger"),
    NGS("Illumina (NGS)"),
    ROUND2("Round 2"),
    FRAGMENT("Fragment");

    @Getter
    private final String value;
    
  }
  
  @JsonApiId
  private Integer pcrBatchId;
  
  private String name;
  
  private PcrBatchType type;
  
  @JsonApiRelation
  private List<PcrReactionDto> reactions;
  
}
