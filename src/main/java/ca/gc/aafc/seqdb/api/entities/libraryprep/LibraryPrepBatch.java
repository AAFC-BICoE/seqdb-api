package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import lombok.Builder;

/**
 * A batch of library preps for samples.
 */
@Entity

@Table(name = "LibraryPrepBatchs")
public class LibraryPrepBatch implements Serializable {

  private static final long serialVersionUID = -397511264904370827L;

  private Integer libraryPrepBatchId;

  private String name;

  private Double totalLibraryYieldNm;

  private String notes;

  private String cleanUpNotes;

  private String yieldNotes;

  private LocalDate dateUsed;

  private Product product;

  private Protocol protocol;

  private ContainerType containerType;

  private PcrProfile thermocyclerProfile;

  private IndexSet indexSet;

  private List<LibraryPrep> libraryPreps;

  /** Default constructor */
  public LibraryPrepBatch() {
  }

  /** Constructor needed to generate builder. */
  @Builder
  public LibraryPrepBatch(String name, Double totalLibraryYieldNm, String notes,
      String cleanUpNotes, String yieldNotes, Product product, Protocol protocol,
      ContainerType containerType, IndexSet indexSet, LocalDate dateUsed) {
    super();
    this.name = name;
    this.totalLibraryYieldNm = totalLibraryYieldNm;
    this.notes = notes;
    this.cleanUpNotes = cleanUpNotes;
    this.yieldNotes = yieldNotes;
    this.product = product;
    this.protocol = protocol;
    this.containerType = containerType;
    this.indexSet = indexSet;
    this.dateUsed = dateUsed;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "LibraryPrepBatchID")
  public Integer getLibraryPrepBatchId() {
    return libraryPrepBatchId;
  }

  public void setLibraryPrepBatchId(Integer libraryPrepBatchId) {
    this.libraryPrepBatchId = libraryPrepBatchId;
  }

  @NotNull
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "TotalLibraryYieldNm")
  public Double getTotalLibraryYieldNm() {
    return totalLibraryYieldNm;
  }

  public void setTotalLibraryYieldNm(Double totalLibraryYieldNm) {
    this.totalLibraryYieldNm = totalLibraryYieldNm;
  }

  @Column(name = "Notes")
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Column(name = "CleanUpNotes")
  public String getCleanUpNotes() {
    return cleanUpNotes;
  }

  public void setCleanUpNotes(String cleanUpNotes) {
    this.cleanUpNotes = cleanUpNotes;
  }

  @Column(name = "YieldNotes")
  public String getYieldNotes() {
    return yieldNotes;
  }

  public void setYieldNotes(String yieldNotes) {
    this.yieldNotes = yieldNotes;
  }

  @Column(name = "DateUsed")
  public LocalDate getDateUsed() {
    return dateUsed;
  }

  public void setDateUsed(LocalDate dateUsed) {
    this.dateUsed = dateUsed;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ProductID")
  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ProtocolID")
  public Protocol getProtocol() {
    return protocol;
  }

  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ContainerTypeID")
  public ContainerType getContainerType() {
    return containerType;
  }

  public void setContainerType(ContainerType containerType) {
    this.containerType = containerType;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ThermocyclerProfileID")
  public PcrProfile getThermocyclerProfile() {
    return thermocyclerProfile;
  }

  public void setThermocyclerProfile(PcrProfile thermocyclerProfile) {
    this.thermocyclerProfile = thermocyclerProfile;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IndexSetID")
  public IndexSet getIndexSet() {
    return indexSet;
  }

  public void setIndexSet(IndexSet indexSet) {
    this.indexSet = indexSet;
  }

  @OneToMany(mappedBy = "libraryPrepBatch")
  public List<LibraryPrep> getLibraryPreps() {
    return libraryPreps;
  }

  public void setLibraryPreps(List<LibraryPrep> libraryPreps) {
    this.libraryPreps = libraryPreps;
  }

  @Transient
  public Integer getId() {
    return this.libraryPrepBatchId;
  }

  public void setId(Integer id) {
    this.libraryPrepBatchId = id;
  }

}
