package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer.PrimerType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "pcrPrimer")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(PcrPrimer.class)
public class PcrPrimerDto {

  @JsonApiId
  private UUID uuid;

  // Required fields

  private String name;

  private PrimerType type;

  private String seq;

  private Integer lotNumber;

  // Optional fields

  private Integer version;

  private String direction;

  private String tmCalculated;

  private Integer tmPe;

  private String position;

  private String note;

  private Timestamp lastModified;

  private String application;

  private String reference;

  private String targetSpecies;

  private String supplier;

  private LocalDate dateOrdered;

  private String purification;

  private String designedBy;

  private String stockConcentration;

  // Optional relations

  @JsonApiRelation
  private RegionDto region;

}
