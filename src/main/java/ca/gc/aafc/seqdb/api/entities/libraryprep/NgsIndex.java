package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
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
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NgsIndexes")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgsIndex {

  public enum NgsIndexDirection {
    I5, I7, FORWARD, REVERSE
  }

  @Getter(onMethod = @__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    }))
  private Integer id;

  @Getter(onMethod = @__({
    @NotNull,
    @NaturalId
    }))
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @NotNull
  private String name;

  private Integer lotNumber;

  @Type(type = "pgsql_enum")
  @Enumerated(EnumType.STRING)
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

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "indexsetid")
    }))
  private IndexSet indexSet;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
