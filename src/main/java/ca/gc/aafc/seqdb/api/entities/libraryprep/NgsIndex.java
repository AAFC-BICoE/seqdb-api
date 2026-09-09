package ca.gc.aafc.seqdb.api.entities.libraryprep;

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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.type.SqlTypes;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ngsindexes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgsIndex implements DinaEntity {

  public enum NgsIndexDirection {
    I5, I7, FORWARD, REVERSE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  @Column(name = "createdby", updatable = false)
  private String createdBy;

  @Column(name = "createdon", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @NotNull
  private String name;

  @Column(name = "lotnumber")
  private Integer lotNumber;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  private NgsIndexDirection direction;

  private String purification;

  @Column(name = "tmcalculated")
  private String tmCalculated;

  @Column(name = "dateordered")
  private LocalDate dateOrdered;

  @Column(name = "datedestroyed")
  private LocalDate dateDestroyed;

  private String application;
  private String reference;
  private String supplier;

  @Column(name = "designedby")
  private String designedBy;

  @Column(name = "stockconcentration")
  private String stockConcentration;

  private String notes;

  @Column(name = "litreference")
  private String litReference;

  @Column(name = "primersequence")
  private String primerSequence;

  @Column(name = "miseqhiseqindexsequence")
  private String miSeqHiSeqIndexSequence;

  @Column(name = "miniseqnextseqindexsequence")
  private String miniSeqNextSeqIndexSequence;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "indexsetid")
  private IndexSet indexSet;

  @Transient
  @Override
  public String getGroup() {
    return this.getIndexSet().getGroup();
  }

}
