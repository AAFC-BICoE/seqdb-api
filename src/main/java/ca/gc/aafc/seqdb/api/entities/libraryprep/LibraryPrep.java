package ca.gc.aafc.seqdb.api.entities.libraryprep;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import ca.gc.aafc.seqdb.api.entities.Sample;
import lombok.Builder;

/**
 * The library prep of a sample.
 */
@Entity

@Table(name = "LibraryPreps")
public class LibraryPrep {

  private Integer libraryPrepId;

  private Double inputNg;

  private String quality;

  private String size;

  private Integer wellColumn;

  private String wellRow;

  private LibraryPrepBatch libraryPrepBatch;

  private Sample sample;

  private NgsIndex indexI5;

  private NgsIndex indexI7;

  public LibraryPrep() {
  }

  @Builder
  public LibraryPrep(Double inputNg, String quality, String size, Integer wellColumn,
      String wellRow, LibraryPrepBatch libraryPrepBatch, Sample sample, NgsIndex indexI5,
      NgsIndex indexI7) {
    super();
    this.inputNg = inputNg;
    this.quality = quality;
    this.size = size;
    this.libraryPrepBatch = libraryPrepBatch;
    this.sample = sample;
    this.indexI5 = indexI5;
    this.indexI7 = indexI7;
    this.wellColumn = wellColumn;
    this.wellRow = wellRow;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "LibraryPrepID")
  public Integer getLibraryPrepId() {
    return libraryPrepId;
  }

  public void setLibraryPrepId(Integer libraryPrepId) {
    this.libraryPrepId = libraryPrepId;
  }

  @Column(name = "InputNg")
  public Double getInputNg() {
    return inputNg;
  }

  public void setInputNg(Double inputNg) {
    this.inputNg = inputNg;
  }

  @Column(name = "Quality")
  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }

  @Column(name = "Size")
  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  @Min(value = 1)
  @Column(name = "WellColumn")
  public Integer getWellColumn() {
    return wellColumn;
  }

  public void setWellColumn(Integer wellColumn) {
    this.wellColumn = wellColumn;
  }

  @Size(max = 2)
  @Pattern(regexp = "[a-zA-Z]")
  @Column(name = "WellRow")
  public String getWellRow() {
    return wellRow;
  }

  public void setWellRow(String wellRow) {
    this.wellRow = wellRow;
  }

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LibraryPrepBatchID")
  public LibraryPrepBatch getLibraryPrepBatch() {
    return libraryPrepBatch;
  }

  public void setLibraryPrepBatch(LibraryPrepBatch libraryPrepBatch) {
    this.libraryPrepBatch = libraryPrepBatch;
  }

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SampleID")
  public Sample getSample() {
    return sample;
  }

  public void setSample(Sample sample) {
    this.sample = sample;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IndexI5ID")
  public NgsIndex getIndexI5() {
    return indexI5;
  }

  public void setIndexI5(NgsIndex indexI5) {
    this.indexI5 = indexI5;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IndexI7ID")
  public NgsIndex getIndexI7() {
    return indexI7;
  }

  public void setIndexI7(NgsIndex indexI7) {
    this.indexI7 = indexI7;
  }

  @Transient
  public Integer getId() {
    return this.libraryPrepId;
  }

  public void setId(Integer id) {
    this.libraryPrepId = id;
  }

}
