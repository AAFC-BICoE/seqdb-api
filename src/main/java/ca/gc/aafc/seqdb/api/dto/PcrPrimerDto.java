package ca.gc.aafc.seqdb.api.dto;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@JsonApiResource(type = "pcrPrimer")
public class PcrPrimerDto implements JsonApiDto {

  @RequiredArgsConstructor
  public enum PrimerType {

    /** The sanger. */
    PRIMER("PCR Primer"),

    /** The mid. */
    MID("454 Multiplex Identifier"),

    /** The fusion primer. */
    FUSION_PRIMER("Fusion Primer"),

    /** The ngs. */
    ILLUMINA_INDEX("Illumina Index");

    /** The value. */
    @Getter
    private final String value;
    
  }
  
  @JsonApiId
  private Integer id;
  
  private String name;
  
  private PrimerType type;
  
  private String seq;
  
  private Integer lotNumber;

}
