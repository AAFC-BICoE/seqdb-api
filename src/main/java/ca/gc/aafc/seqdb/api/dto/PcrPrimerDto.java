package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.util.Date;

import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "pcrPrimer")
public class PcrPrimerDto {

  @JsonApiId
  private Integer pcrPrimerId;

  // Required fields

  private String name;

  private PrimerType type;

  private String seq;

  private Integer lotNumber;

  // Optional fields

  private Integer version;

  private Date designDate;

  private String direction;

  private String tmCalculated;

  private Integer tmPe;

  private String position;

  private String storage;

  private String restrictionSite;

  private Boolean used4sequencing;

  private Boolean used4qrtpcr;

  private Boolean used4nestedPcr;

  private Boolean used4genotyping;

  private Boolean used4cloning;

  private Boolean used4stdPcr;

  private String referenceSeqDir;

  private String referenceSeqFile;

  private String urllink;

  private String note;

  private Timestamp lastModified;

  private String application;

  private String reference;

  private String targetSpecies;

  private String supplier;

  private Date dateOrdered;

  private String purification;

  private String designedBy;

  private String stockConcentration;

  // Optional relations

  @JsonApiRelation
  private RegionDto region;

  @JsonApiRelation
  private GroupDto group;

}
