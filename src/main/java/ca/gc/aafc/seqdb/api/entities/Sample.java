/*
 * =====================================================================
 * Class:		Sample.java
 * Package: 	ca.gc.aafc.seqdb.api.entities
 * 
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Agriculture and Agri-Food Canada http://www.agr.gc.ca/
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =====================================================================
 * 
 */
package ca.gc.aafc.seqdb.api.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;

@Entity
@Table(name = "Samples", uniqueConstraints = {
@UniqueConstraint(columnNames = { "Name", "Version" }) })
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Sample {

  public enum SampleType {

    NO_TYPE("No Sample Type"), DNA("DNA"), RNA("RNA"), PROTEIN("Protein"), LIBRARY(
        "Library"), CHEMICAL("Chemical");

    private final String value;

    /**
     * Instantiates a new sample type.
     *
     * @param value
     *          the value
     */
    SampleType(String value) {
      this.value = value;
    }

    /**
     * Gets the value of the value field.
     *
     * @return the value
     */
    public String getValue() {
      return value;
    }

    /**
     * Returns the Sample Type ( Wrapped in an Optional )based off a case insensitive match of its
     * value. Or an empty Optional if a match could not be made.
     * 
     * @param searchValue
     *          - sample type to search
     * @return - the sample type or empty wrapped in an Optional
     */
    public static Optional<SampleType> getByValue(String searchValue) {
      for (SampleType type : SampleType.values()) {
        if (StringUtils.equalsIgnoreCase(type.getValue(), searchValue)) {
          return Optional.of(type);
        }
      }
      return Optional.empty();
    }
  }

  /** The sample id. */
  private Integer sampleId;

  /** The name. */
  private String name;

  /** The experimenter. */
  private String experimenter;

  /** The version. */
  private String version;

  /** The version description. */
  private String versionDescription;

  /** The treatment. */
  private String treatment;

  /** The source. */
  private String source;

  /** The dna concentration. */
  private String dnaConcentration;

  /** The dna concentration notes. */
  private String dnaConcentrationNotes;

  /** The dna concentration per material. */
  private String dnaConcentrationPerStartMaterial;

  /** The date. */
  private Date date;

  /** The nucl acid ext. */
  private String nuclAcidExt;

  /** The extraction batch. */
  private String extractionBatch;

  /** The pellet size. */
  private Double pelletSize;

  /** The lysis buffer volume. */
  private Double lysisBufferVolume;

  /** The proteinase K volume. */
  private Double proteinaseKVolume;

  /** The qubit DNA concentration. */
  private Double qubitDNAConcentration;

  /** The quantification method. */
  private String quantificationMethod;

  /** The growth media. */
  private String growthMedia;

  /** The dna notes. */
  private String dnaNotes;

  /** The notes. */
  private String notes;

  /** The tube id. */
  private String tubeId;

  /** The unusable dna. */
  private Boolean unusableDna;

  /** The last modified. */
  private Timestamp lastModified;

  /** The kit. */
  private Product kit;

  /** The protocol. */
  private Protocol protocol;

  private String discardedNotes;

  private LocalDate dateDiscarded;

  private SampleType sampleType;

  private LocalDate inoculationDate;

  private String fraction;

  private Double fermentationTemperature;

  private String fermentationTime;

  private String extractionSolvent;
  
  /** The Date Archived. */
  private String dateArchived;
  
  /** The Sample Storage Location. */
  private String sampleStorageLocation;  
  
  /** The Sample Storage Conditions. */
  private String sampleStorageConditions;  
  
  /** The Amount Available. */
  private String amountAvailable;    
  
  /** The Pre-treatment . */
  private String pretreatment;    
  
  /** The Alternative Contact Information. */
  private String alternativeContactInfo ;      

  // Constructors
  /**
   * Instantiates a new sample.
   */
  public Sample() {
  }

  /**
   * Instantiates a new sample.
   *
   * @param name
   *          the name
   * @param experimenter
   *          the experimenter
   * @param version
   *          the version
   * @param versionDescription
   *          the version description
   * @param treatment
   *          the treatment
   * @param source
   *          the source
   * @param dnaConcentration
   *          the dna concentration
   * @param dnaConcentrationNotes
   *          the dna concentration notes
   * @param dnaConcentrationPerStartMaterial
   *          the dna concentration per start material
   * @param date
   *          the date
   * @param nuclAcidExt
   *          the nucl acid ext
   * @param extractionBatch
   *          the extraction batch
   * @param pelletSize
   *          the pellet size
   * @param lysisBufferVolume
   *          the lysis buffer volume
   * @param proteinaseKVolume
   *          the proteinase K volume
   * @param qubitDNAConcentration
   *          the qubit DNA concentration
   * @param quantificationMethod
   *          the quantification method
   * @param growthMedia
   *          the growth media
   * @param dnaNotes
   *          the dna notes
   * @param notes
   *          the notes
   * @param tubeId
   *          the tube id
   * @param unusableDna
   *          the unusable dna
   * @param kit
   *          the kit
   * @param specimenReplicate
   *          the specimen replicate
   * @param mixedSpecimen
   *          the mixed specimen
   * @param location
   *          the location
   * @param group
   *          the group
   * @param protocol
   *          the protocol
   * @param sampleType
   *          the sample type
   * @param inoculationDate
   *          the inoculation date
   * @param fraction
   *          the fraction
   * @param fermentationTemperature
   *          the fermentation temperature
   * @param fermentationTime
   *          the fermentation time
   * @param extractionSolvent
   *          the extraction solvent
   */
  @Builder
  public Sample(String name, String experimenter, String version, String versionDescription,
      String treatment, String source, String dnaConcentration, String dnaConcentrationNotes,
      String dnaConcentrationPerStartMaterial, Date date, String nuclAcidExt,
      String extractionBatch, Double pelletSize, Double lysisBufferVolume, Double proteinaseKVolume,
      Double qubitDNAConcentration,
      String quantificationMethod, String growthMedia, String dnaNotes, String notes, String tubeId,
      Boolean unusableDna, Product kit, Protocol protocol,
      SampleType sampleType, LocalDate inoculationDate, String fraction,
      Double fermentationTemperature, String fermentationTime, String extractionSolvent,
      String discardedNotes, LocalDate dateDiscarded,
      String dateArchived, String sampleStorageLocation, String sampleStorageConditions, String amountAvailable, String pretreatment, String alternativeContactInfo) {
    super();
    this.name = name;
    this.experimenter = experimenter;
    this.version = version;
    this.versionDescription = versionDescription;
    this.treatment = treatment;
    this.source = source;
    this.kit = kit;
    this.dnaConcentration = dnaConcentration;
    this.dnaConcentrationNotes = dnaConcentrationNotes;
    this.dnaConcentrationPerStartMaterial = dnaConcentrationPerStartMaterial;
    this.date = date;
    this.nuclAcidExt = nuclAcidExt;
    this.extractionBatch = extractionBatch;
    this.pelletSize = pelletSize;
    this.lysisBufferVolume = lysisBufferVolume;
    this.proteinaseKVolume = proteinaseKVolume;
    this.qubitDNAConcentration = qubitDNAConcentration;
    this.quantificationMethod = quantificationMethod;
    this.growthMedia = growthMedia;
    this.dnaNotes = dnaNotes;
    this.notes = notes;
    this.tubeId = tubeId;
    this.unusableDna = unusableDna;
    this.protocol = protocol;
    this.sampleType = sampleType;
    this.inoculationDate = inoculationDate;
    this.fraction = fraction;
    this.fermentationTemperature = fermentationTemperature;
    this.fermentationTime = fermentationTime;
    this.extractionSolvent = extractionSolvent;
    this.discardedNotes = discardedNotes;
    this.dateDiscarded = dateDiscarded;
    this.dateArchived = dateArchived;
    this.sampleStorageLocation = sampleStorageLocation;
    this.sampleStorageConditions = sampleStorageConditions;
    this.amountAvailable = amountAvailable;
    this.pretreatment = pretreatment;
    this.alternativeContactInfo = alternativeContactInfo;
  }

  // Property accessors
  /**
   * Gets the sample id.
   *
   * @return the sample id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "SampleID")
  public Integer getSampleId() {
    return this.sampleId;
  }

  /**
   * Sets the sample id.
   *
   * @param sampleId
   *          the new sample id
   */
  public void setSampleId(Integer sampleId) {
    this.sampleId = sampleId;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @NotNull
  @Size(max = 50)
  @Column(name = "Name")
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the experimenter.
   *
   * @return the experimenter
   */
  @Size(max = 50)
  @Column(name = "Experimenter")
  public String getExperimenter() {
    return experimenter;
  }

  /**
   * Sets the experimenter.
   *
   * @param experimenter
   *          the new experimenter
   */
  public void setExperimenter(String experimenter) {
    this.experimenter = experimenter;
  }

  /**
   * Gets the version.
   *
   * @return the version
   */
  @NotNull
  @Size(max = 50)
  @Column(name = "Version", columnDefinition = "varchar(50) DEFAULT ''")
  public String getVersion() {
    return this.version;
  }

  /**
   * Sets the version.
   *
   * @param version
   *          the new version
   */
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Gets the version description.
   *
   * @return the version description
   */
  @Size(max = 512)
  @Column(name = "VersionDescription")
  public String getVersionDescription() {
    return this.versionDescription;
  }

  /**
   * Sets the version description.
   *
   * @param versionDescription
   *          the new version description
   */
  public void setVersionDescription(String versionDescription) {
    this.versionDescription = versionDescription;
  }

  /**
   * Gets the source.
   *
   * @return the source
   */
  @Size(max = 200)
  @Column(name = "Source")
  public String getSource() {
    return this.source;
  }

  /**
   * Sets the source.
   *
   * @param source
   *          the new source
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Gets the dna concentration.
   *
   * @return the dna concentration
   */
  @Size(max = 50)
  @Column(name = "DnaConcentration")
  public String getDnaConcentration() {
    return dnaConcentration;
  }

  /**
   * Sets the dna concentration.
   *
   * @param dnaConcentration
   *          the new dna concentration
   */
  public void setDnaConcentration(String dnaConcentration) {
    this.dnaConcentration = dnaConcentration;
  }

  /**
   * @return the dnaConcentrationNotes
   */
  @Size(max = 200)
  @Column(name = "DnaConcentrationNotes")
  public String getDnaConcentrationNotes() {
    return dnaConcentrationNotes;
  }

  /**
   * @param dnaConcentrationNotes
   *          the dnaConcentrationNotes to set
   */
  public void setDnaConcentrationNotes(String dnaConcentrationNotes) {
    this.dnaConcentrationNotes = dnaConcentrationNotes;
  }

  /**
   * Gets the dna concentration per start material.
   *
   * @return the dna concentration per start material
   */
  @Size(max = 50)
  @Column(name = "DnaConcentrationPerStartMaterial")
  public String getDnaConcentrationPerStartMaterial() {
    return dnaConcentrationPerStartMaterial;
  }

  /**
   * Sets the dna concentration per start material.
   *
   * @param dnaConcentrationPerStartMaterial
   *          the new dna concentration per start material
   */
  public void setDnaConcentrationPerStartMaterial(String dnaConcentrationPerStartMaterial) {
    this.dnaConcentrationPerStartMaterial = dnaConcentrationPerStartMaterial;
  }

  /**
   * Gets the treatment.
   *
   * @return the treatment
   */
  @Column(name = "Treatment")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
  public String getTreatment() {
    return this.treatment;
  }

  /**
   * Sets the treatment.
   *
   * @param treatment
   *          the new treatment
   */
  public void setTreatment(String treatment) {
    this.treatment = treatment;
  }

  /**
   * Gets the growth media.
   *
   * @return the growth media
   */
  @Size(max = 50)
  @Column(name = "GrowthMedia")
  public String getGrowthMedia() {
    return this.growthMedia;
  }

  /**
   * Sets the growth media.
   *
   * @param growthMedia
   *          the new growth media
   */
  public void setGrowthMedia(String growthMedia) {
    this.growthMedia = growthMedia;
  }

  /**
   * Gets the date.
   *
   * @return the date
   */
  @Column(name = "Date")
  public Date getDate() {
    return date;
  }

  /**
   * Sets the date.
   *
   * @param date
   *          the new date
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Gets the nucl acid ext.
   *
   * @return the nucl acid ext
   */
  @Size(max = 50)
  @Column(name = "NuclAcidExt")
  public String getNuclAcidExt() {
    return nuclAcidExt;
  }

  /**
   * Sets the nucl acid ext.
   *
   * @param nuclAcidExt
   *          the new nucl acid ext
   */
  public void setNuclAcidExt(String nuclAcidExt) {
    this.nuclAcidExt = nuclAcidExt;
  }

  /**
   * @return the extractionBatch
   */
  @Size(max = 100)
  @Column(name = "ExtractionBatch")
  public String getExtractionBatch() {
    return extractionBatch;
  }

  /**
   * @param extractionBatch
   *          the extractionBatch to set
   */
  public void setExtractionBatch(String extractionBatch) {
    this.extractionBatch = extractionBatch;
  }

  /**
   * Gets the pellet size.
   *
   * @return the pellet size
   */
  @Column(name = "PelletSize")
  public Double getPelletSize() {
    return pelletSize;
  }

  /**
   * Sets the pellet size.
   *
   * @param pelletSize
   *          the new pellet size
   */
  public void setPelletSize(Double pelletSize) {
    this.pelletSize = pelletSize;
  }

  /**
   * Gets the lysis buffer volume.
   *
   * @return the lysis buffer volume
   */
  @Column(name = "LysisBufferVolume")
  public Double getLysisBufferVolume() {
    return lysisBufferVolume;
  }

  /**
   * Sets the lysis buffer volume.
   *
   * @param lysisBufferVolume
   *          the new lysis buffer volume
   */
  public void setLysisBufferVolume(Double lysisBufferVolume) {
    this.lysisBufferVolume = lysisBufferVolume;
  }

  /**
   * Gets the proteinase K volume.
   *
   * @return the proteinase K volume
   */
  @Column(name = "ProteinaseKVolume")
  public Double getProteinaseKVolume() {
    return proteinaseKVolume;
  }

  /**
   * Sets the proteinase K volume.
   *
   * @param proteinaseKVolume
   *          the new proteinase K volume
   */
  public void setProteinaseKVolume(Double proteinaseKVolume) {
    this.proteinaseKVolume = proteinaseKVolume;
  }

  /**
   * Gets the qubit DNA concentration.
   *
   * @return the qubit DNA concentration
   */
  @Column(name = "QubitDNAConcentration")
  public Double getQubitDNAConcentration() {
    return qubitDNAConcentration;
  }

  /**
   * Sets the qubit DNA concentration.
   *
   * @param qubitDNAConcentration
   *          the new qubit DNA concentration
   */
  public void setQubitDNAConcentration(Double qubitDNAConcentration) {
    this.qubitDNAConcentration = qubitDNAConcentration;
  }

  /**
   * Gets the quantification method.
   *
   * @return the quantification method
   */
  @Size(max = 100)
  @Column(name = "QuantificationMethod")
  public String getQuantificationMethod() {
    return quantificationMethod;
  }

  /**
   * Sets the quantification method.
   *
   * @param quantificationMethod
   *          the new quantification method
   */
  public void setQuantificationMethod(String quantificationMethod) {
    this.quantificationMethod = quantificationMethod;
  }

  /**
   * Gets the notes.
   *
   * @return the notes
   */
  @Size(max = 1024)
  @Column(name = "Notes")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
  public String getNotes() {
    return notes;
  }

  /**
   * Sets the notes.
   *
   * @param notes
   *          the new notes
   */
  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * Gets the dna notes.
   *
   * @return the dna notes
   */
  @Size(max = 1024)
  @Column(name = "DnaNotes")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
  public String getDnaNotes() {
    return dnaNotes;
  }

  /**
   * Sets the dna notes.
   *
   * @param dnaNotes
   *          the new dna notes
   */
  public void setDnaNotes(String dnaNotes) {
    this.dnaNotes = dnaNotes;
  }

  /**
   * Gets the tube id.
   *
   * @return the tube id
   */
  @Size(max = 50)
  @Column(name = "TubeId")
  public String getTubeId() {
    return tubeId;
  }

  /**
   * Sets the tube id.
   *
   * @param tubeId
   *          the new tube id
   */
  public void setTubeId(String tubeId) {
    this.tubeId = tubeId;
  }

  /**
   * Gets the unusable dna.
   *
   * @return the unusable dna
   */
  @Column(name = "UnusableDna")
  public Boolean getUnusableDna() {
    return unusableDna;
  }

  /**
   * Sets the unusable dna.
   *
   * @param unusableDna
   *          the new unusable dna
   */
  public void setUnusableDna(Boolean unusableDna) {
    this.unusableDna = unusableDna;
  }

  /**
   * Gets the kit.
   *
   * @return the kit
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "ProductID")
  public Product getKit() {
    return this.kit;
  }

  /**
   * Sets the kit.
   *
   * @param kit
   *          the new kit
   */
  public void setKit(Product kit) {
    this.kit = kit;
  }

  /**
   * Gets the protocol.
   *
   * @return the protocol
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "ProtocolID")
  public Protocol getProtocol() {
    return protocol;
  }

  /**
   * Sets the protocol.
   *
   * @param protocol
   *          the new protocol
   */
  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }

  @Column(name = "DiscardedNotes")
  public String getDiscardedNotes() {
    return discardedNotes;
  }

  public void setDiscardedNotes(String discardedNotes) {
    this.discardedNotes = discardedNotes;
  }

  @Column(name = "DateDiscarded")
  public LocalDate getDateDiscarded() {
    return dateDiscarded;
  }

  public void setDateDiscarded(LocalDate dateDiscarded) {
    this.dateDiscarded = dateDiscarded;
  }

  /**
   * Gets the last modified.
   *
   * @return the last modified
   */
  @Version
  @Column(name = "LastModified")
  public Timestamp getLastModified() {
    return this.lastModified;
  }

  /*
   * Note that with @Version defined for the lastModified field, every time this object is created
   * or modified, lastModified is set automatically, so no explicit set is needed for this field.
   */
  /**
   * Sets the last modified.
   *
   * @param lastModified
   *          the new last modified
   */
  public void setLastModified(Timestamp lastModified) {
    this.lastModified = lastModified;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getUniqueId()
   */
  @Transient
  public Integer getId() {
    return getSampleId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setSampleId(id);
  }

  /**
   * Returns the value of the sample type field
   * 
   * @return value of the sample type field
   */
  @Column(name = "SampleType")
  @Enumerated(EnumType.STRING)
  public SampleType getSampleType() {
    return sampleType;
  }

  /**
   * Sets the value of the sample type field
   * 
   * @param sampleType
   *          value of sample type to set
   */
  public void setSampleType(SampleType sampleType) {
    this.sampleType = sampleType;
  }

  /**
   * Returns the value of the inoculation date field
   * 
   * @return - value of inoculation date field
   */
  @Column(name = "InoculationDate")
  public LocalDate getInoculationDate() {
    return inoculationDate;
  }

  /**
   * Sets the value of the inoculation date field
   * 
   * @param inoculationDate
   *          value to set
   */
  public void setInoculationDate(LocalDate inoculationDate) {
    this.inoculationDate = inoculationDate;
  }

  /**
   * Returns the value of the fraction field
   * 
   * @return - value of fraction field
   */
  @Size(max = 50)
  @Column(name = "Fraction")
  public String getFraction() {
    return fraction;
  }

  /**
   * Sets the value of the fraction field
   * 
   * @param fraction
   *          value to set
   */
  public void setFraction(String fraction) {
    this.fraction = fraction;
  }

  /**
   * Returns the value of the fermentation temperature
   * 
   * @return value of fermentation temperature field
   */
  @Column(name = "FermentationTemperature")
  public Double getFermentationTemperature() {
    return fermentationTemperature;
  }

  /**
   * Sets the value of the fermentation temperature
   * 
   * @param fermentationTemperature
   *          - value to set
   */
  public void setFermentationTemperature(Double fermentationTemperature) {
    this.fermentationTemperature = fermentationTemperature;
  }

  /**
   * Returns the value of the fermentation time field
   * 
   * @return - value of fermentation time field
   */
  @Size(max = 50)
  @Column(name = "FermentationTime")
  public String getFermentationTime() {
    return fermentationTime;
  }

  /**
   * Sets the value of the fermentation time field
   * 
   * @param fermentationTime
   *          - value to set
   */
  public void setFermentationTime(String fermentationTime) {
    this.fermentationTime = fermentationTime;
  }

  /**
   * Returns the value of the extraction solvent field
   * 
   * @return - value of extraction solvent field
   */
  @Size(max = 50)
  @Column(name = "ExtractionSolvent")
  public String getExtractionSolvent() {
    return extractionSolvent;
  }

  /**
   * Sets value for the exraction solvent field
   * 
   * @param extractionSolvent
   *          - value to set
   */
  public void setExtractionSolvent(String extractionSolvent) {
    this.extractionSolvent = extractionSolvent;
  }

  /**
   * @return the dateArchived
   */
  @Size(max = 10)
  @Column(name = "DateArchived")  
  public String getDateArchived() {
    return dateArchived;
  }

  /**
   * @param dateArchived the dateArchived to set
   */
  public void setDateArchived(String dateArchived) {
    this.dateArchived = dateArchived;
  }

  /**
   * @return the sampleStorageLocation
   */
  @Size(max = 50)
  @Column(name = "SampleStorageLocation") 
  public String getSampleStorageLocation() {
    return sampleStorageLocation;
  }

  /**
   * @param sampleStorageLocation the sampleStorageLocation to set
   */
  public void setSampleStorageLocation(String sampleStorageLocation) {
    this.sampleStorageLocation = sampleStorageLocation;
  }

  /**
   * @return the sampleStorageConditions
   */
  @Size(max = 50)
  @Column(name = "SampleStorageConditions")  
  public String getSampleStorageConditions() {
    return sampleStorageConditions;
  }

  /**
   * @param sampleStorageConditions the sampleStorageConditions to set
   */
  public void setSampleStorageConditions(String sampleStorageConditions) {
    this.sampleStorageConditions = sampleStorageConditions;
  }

  /**
   * @return the amountAvailable
   */
  @Size(max = 50)
  @Column(name = "AmountAvailable") 
  public String getAmountAvailable() {
    return amountAvailable;
  }

  /**
   * @param amountAvailable the amountAvailable to set
   */
  public void setAmountAvailable(String amountAvailable) {
    this.amountAvailable = amountAvailable;
  }

  /**
   * @return the pretreatment
   */
  @Size(max = 50)
  @Column(name = "Pretreatment") 
  public String getPretreatment() {
    return pretreatment;
  }

  /**
   * @param pretreatment the pretreatment to set
   */
  public void setPretreatment(String pretreatment) {
    this.pretreatment = pretreatment;
  }

  /**
   * @return the alternativeContactInfo
   */
  @Size(max = 100)
  @Column(name = "AlternativeContactInfo") 
  public String getAlternativeContactInfo() {
    return alternativeContactInfo;
  }

  /**
   * @param alternativeContactInfo the alternativeContactInfo to set
   */
  public void setAlternativeContactInfo(String alternativeContactInfo) {
    this.alternativeContactInfo = alternativeContactInfo;
  }

}
