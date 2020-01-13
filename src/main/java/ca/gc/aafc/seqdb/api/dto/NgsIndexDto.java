package ca.gc.aafc.seqdb.api.dto;

import java.time.LocalDate;

import ca.gc.aafc.seqdb.entities.libraryprep.NgsIndex.NgsIndexDirection;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "ngsIndex")
public class NgsIndexDto {
  @JsonApiId
  private Integer ngsIndexId;

  private String name;
  private Integer lotNumber;
  private NgsIndexDirection direction;
  private String purification;
  private String tmCalculated;
  private LocalDate dateOrdered;
  private LocalDate dateDestroyed;
  private String application;
  private String reference;
  private String supplier;
  private String designedBy;
  private String stockConcentration;
  private String notes;
  private String litReference;
  private String primerSequence;
  private String miSeqHiSeqIndexSequence;
  private String miniSeqNextSeqIndexSequence;

  @JsonApiRelation
  private IndexSetDto indexSet;

}
