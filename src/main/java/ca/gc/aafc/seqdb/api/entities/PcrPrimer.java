package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PcrPrimers", uniqueConstraints = {
  @UniqueConstraint(columnNames = { "Name", "LotNumber" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PcrPrimer implements DinaEntity {

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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @NotNull
  @NaturalId
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @Column(name = "groupname")
  private String group;

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

  private String note;

  @Version
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "regionid")
  private Region region;
  
  private LocalDate dateDestroyed;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pooledprimerid")
  private PcrPrimer pooledPrimer;

}
