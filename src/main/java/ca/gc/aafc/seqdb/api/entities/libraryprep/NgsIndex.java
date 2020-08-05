package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;



import lombok.Builder;

@Entity

@Table(name = "NgsIndexes")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class NgsIndex implements Serializable {

  private static final long serialVersionUID = 8368749265881611133L;

  public enum NgsIndexDirection {
    I5, I7, FORWARD, REVERSE
  }

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
  private IndexSet indexSet;

  public NgsIndex() {
  }

  @Builder
  public NgsIndex(String name, Integer lotNumber, NgsIndexDirection direction, String purification,
      String tmCalculated, LocalDate dateOrdered, LocalDate dateDestroyed, String application,
      String reference, String supplier, String designedBy, String stockConcentration, String notes,
      String litReference, String primerSequence, String miSeqHiSeqIndexSequence,
      String miniSeqNextSeqIndexSequence, IndexSet indexSet) {
    super();
    this.name = name;
    this.lotNumber = lotNumber;
    this.direction = direction;
    this.purification = purification;
    this.tmCalculated = tmCalculated;
    this.dateOrdered = dateOrdered;
    this.dateDestroyed = dateDestroyed;
    this.application = application;
    this.reference = reference;
    this.supplier = supplier;
    this.designedBy = designedBy;
    this.stockConcentration = stockConcentration;
    this.notes = notes;
    this.litReference = litReference;
    this.primerSequence = primerSequence;
    this.miSeqHiSeqIndexSequence = miSeqHiSeqIndexSequence;
    this.miniSeqNextSeqIndexSequence = miniSeqNextSeqIndexSequence;
    this.indexSet = indexSet;
  }

  @Id
  @GeneratedValue(generator = "ngs-index-sequence-generator")
  @GenericGenerator(name = "ngs-index-sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
      @Parameter(name = "sequence_name", value = "ngsindexes_ngsindexid_seq"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "10") })
  @Column(name = "NgsIndexID")
  public Integer getNgsIndexId() {
    return ngsIndexId;
  }

  public void setNgsIndexId(Integer ngsIndexId) {
    this.ngsIndexId = ngsIndexId;
  }

  @NotNull
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "LotNumber")
  public Integer getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(Integer lotNumber) {
    this.lotNumber = lotNumber;
  }

  @Type(type = "pgsql_enum")
  @Enumerated(EnumType.STRING)
  @Column(name = "Direction")
  public NgsIndexDirection getDirection() {
    return direction;
  }

  public void setDirection(NgsIndexDirection direction) {
    this.direction = direction;
  }

  @Column(name = "Purification")
  public String getPurification() {
    return purification;
  }

  public void setPurification(String purification) {
    this.purification = purification;
  }

  @Column(name = "TmCalculated")
  public String getTmCalculated() {
    return tmCalculated;
  }

  public void setTmCalculated(String tmCalculated) {
    this.tmCalculated = tmCalculated;
  }

  @Column(name = "DateOrdered")
  public LocalDate getDateOrdered() {
    return dateOrdered;
  }

  public void setDateOrdered(LocalDate dateOrdered) {
    this.dateOrdered = dateOrdered;
  }

  @Column(name = "DateDestroyed")
  public LocalDate getDateDestroyed() {
    return dateDestroyed;
  }

  public void setDateDestroyed(LocalDate dateDestroyed) {
    this.dateDestroyed = dateDestroyed;
  }

  @Column(name = "Application")
  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

  @Column(name = "Reference")
  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  @Column(name = "Supplier")
  public String getSupplier() {
    return supplier;
  }

  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }

  @Column(name = "DesignedBy")
  public String getDesignedBy() {
    return designedBy;
  }

  public void setDesignedBy(String designedBy) {
    this.designedBy = designedBy;
  }

  @Column(name = "StockConcentration")
  public String getStockConcentration() {
    return stockConcentration;
  }

  public void setStockConcentration(String stockConcentration) {
    this.stockConcentration = stockConcentration;
  }

  @Column(name = "Notes")
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Column(name = "LitReference")
  public String getLitReference() {
    return litReference;
  }

  public void setLitReference(String litReference) {
    this.litReference = litReference;
  }

  @Column(name = "PrimerSequence")
  public String getPrimerSequence() {
    return primerSequence;
  }

  public void setPrimerSequence(String primerSequence) {
    this.primerSequence = primerSequence;
  }

  @Column(name = "MiSeqHiSeqIndexSequence")
  public String getMiSeqHiSeqIndexSequence() {
    return miSeqHiSeqIndexSequence;
  }

  public void setMiSeqHiSeqIndexSequence(String miSeqHiSeqIndexSequence) {
    this.miSeqHiSeqIndexSequence = miSeqHiSeqIndexSequence;
  }

  @Column(name = "MiniSeqNextSeqIndexSequence")
  public String getMiniSeqNextSeqIndexSequence() {
    return miniSeqNextSeqIndexSequence;
  }

  public void setMiniSeqNextSeqIndexSequence(String miniSeqNextSeqIndexSequence) {
    this.miniSeqNextSeqIndexSequence = miniSeqNextSeqIndexSequence;
  }

  @NotAudited
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IndexSetID")
  public IndexSet getIndexSet() {
    return indexSet;
  }

  public void setIndexSet(IndexSet indexSet) {
    this.indexSet = indexSet;
  }

  @Transient
  public Integer getId() {
    return this.ngsIndexId;
  }

  public void setId(Integer id) {
    this.ngsIndexId = id;
  }

}
