package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PcrPrimers", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "Name", "LotNumber" }) })
    @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class PcrPrimer {

  public static final String DIRECTION_FORWARD = "F";
  public static final String DIRECTION_REVERSE = "R";

  @AllArgsConstructor
  public enum PrimerType {
    PRIMER("PCR Primer"),
    MID("454 Multiplex Identifier"),
    FUSION_PRIMER("Fusion Primer"),
    ILLUMINA_INDEX("Illumina Index"),
    ITRU_PRIMER("iTru Primer");

    @Getter
    private final String value;
  }

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private PrimerType type;

  @NotNull
  @Size(max = 191)
  private String name;

  @NotNull
  private Integer lotNumber;

  private Integer version;

  @Size(max = 255)
  private String seq;

  @Size(max = 1)
  @Pattern(regexp = "[FR]")
  private String direction;
  
  @Size(max = 11)
  private String tmCalculated;
  private Integer tmPe;

  @Size(max = 10)
  private String position;

  @Size(max = 50)
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

  @Getter(onMethod=@__({
    @Version
  }))
  private Timestamp lastModified;

  @Size(max = 200)
  private String application;
  private String reference;

  @Size(max = 50)
  @Pattern(regexp = "\\d+")
  private String sequenceLength;

  @Size(max = 50)
  private String targetSpecies;

  @Size(max = 50)
  private String supplier;
  private LocalDate dateOrdered;

  @Size(max = 50)
  private String purification;

  @Size(max = 50)
  private String designedBy;

  @Size(max = 10)
  private String stockConcentration;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "regionid")
  }))
  private Region region;
  
  @Size(max = 512)
  private String litReference;

  private LocalDate dateDestroyed;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "pooledprimerid")
  }))
  private PcrPrimer pooledPrimer;

  @NotNull
  private UUID uuid;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }
}
