/*
 * =====================================================================
 * Class:		PcrPrimer.java
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

import java.sql.Timestamp;
import java.time.LocalDate;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Builder;

/**
 * The Class PcrPrimer.
 */
@Entity

@Table(name = "PcrPrimers", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "Name", "LotNumber" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class PcrPrimer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 8053203319426929005L;

  /** The Constant DIRECTION_FORWARD. */
  public static final String DIRECTION_FORWARD = "F";

  /** The Constant DIRECTION_REVERSE. */
  public static final String DIRECTION_REVERSE = "R";

  public enum PrimerType {

    /** The sanger. */
    PRIMER("PCR Primer"),

    /** The mid. */
    MID("454 Multiplex Identifier"),

    /** The fusion primer. */
    FUSION_PRIMER("Fusion Primer"),

    /** The ngs. */
    ILLUMINA_INDEX("Illumina Index"),

    /** The iTru Primer. */
    ITRU_PRIMER("iTru Primer");

    /** The value. */
    private final String value;

    /**
     * Instantiates a new protocol type.
     *
     * @param value
     *          the value
     */
    PrimerType(String value) {
      this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
      return value;
    }
  }

  /** The pcr primer id. */
  private Integer pcrPrimerId;

  /** The type. */
  private PrimerType type;

  /** The name. */
  private String name;

  /** The version. */
  private Integer version;

  /** The seq. */
  private String seq;

  /** The direction. */
  private String direction;

  /** The tm calculated. */
  private String tmCalculated;

  /** The tm pe. */
  private Integer tmPe;

  /** The position. */
  private String position;

  /** The storage. */
  private String storage;

  /** The restriction site. */
  private String restrictionSite;

  /** The used4sequencing. */
  private Boolean used4sequencing;

  /** The used4qrtpcr. */
  private Boolean used4qrtpcr;

  /** The used4nested pcr. */
  private Boolean used4nestedPcr;

  /** The used4genotyping. */
  private Boolean used4genotyping;

  /** The used4cloning. */
  private Boolean used4cloning;

  /** The used4std pcr. */
  private Boolean used4stdPcr;

  /** The reference seq dir. */
  private String referenceSeqDir;

  /** The reference seq file. */
  private String referenceSeqFile;

  /** The urllink. */
  private String urllink;

  /** The note. */
  private String note;

  /** The last modified. */
  private Timestamp lastModified;

  /** The application. */
  private String application;

  /** The reference. */
  private String reference;

  /** The sequence length. */
  private String sequenceLength;

  /** The target species. */
  private String targetSpecies;

  /** The supplier. */
  private String supplier;

  /** The date ordered. */
  private LocalDate dateOrdered;

  /** The purification. */
  private String purification;

  /** The designed by. */
  private String designedBy;

  /** The stock concentration. */
  private String stockConcentration;

  /** The lot number. */
  private Integer lotNumber;

  /** The region. */
  private Region region;

  /** The lit reference. */
  private String litReference;

  /** The date destroyed. */
  private LocalDate dateDestroyed;

  /** The pooled primer. */
  private PcrPrimer pooledPrimer;

  /** Unique UUID */
  private UUID uuid;

  // Constructors

  /**
   * Instantiates a new pcr primer.
   */
  public PcrPrimer() {
  }

  /**
   * Instantiates a new pcr primer.
   *
   * @param type
   *          the type
   * @param name
   *          the name
   * @param version
   *          the version
   * @param seq
   *          the seq
   * @param direction
   *          the direction
   * @param storage
   *          the storage
   * @param used4sequencing
   *          the used4sequencing
   * @param used4qrtpcr
   *          the used4qrtpcr
   * @param used4nestedPcr
   *          the used4nested pcr
   * @param used4genotyping
   *          the used4genotyping
   * @param used4cloning
   *          the used4cloning
   * @param used4stdPcr
   *          the used4std pcr
   * @param sequenceLength
   *          the sequence length
   * @param region
   *          the region
   * @param group
   *          the group
   * @param pooledPrimer
   *          the pooled primer
   */
  public PcrPrimer(PrimerType type, String name, Integer version, String seq, String direction,
      String storage, Boolean used4sequencing, Boolean used4qrtpcr, Boolean used4nestedPcr,
      Boolean used4genotyping, Boolean used4cloning, Boolean used4stdPcr, String sequenceLength,
      Region region, PcrPrimer pooledPrimer) {
    this.type = type;
    this.name = name;
    this.version = version;
    this.seq = seq;
    this.direction = direction;
    this.storage = storage;
    this.used4sequencing = used4sequencing;
    this.used4qrtpcr = used4qrtpcr;
    this.used4nestedPcr = used4nestedPcr;
    this.used4genotyping = used4genotyping;
    this.used4cloning = used4cloning;
    this.used4stdPcr = used4stdPcr;
    this.sequenceLength = sequenceLength;
    this.region = region;
    this.pooledPrimer = pooledPrimer;
  }

  /**
   * Instantiates a new pcr primer.
   *
   * @param type
   *          the type
   * @param name
   *          the name
   * @param version
   *          the version
   * @param seq
   *          the seq
   * @param direction
   *          the direction
   * @param tmCalculated
   *          the tm calculated
   * @param tmPe
   *          the tm pe
   * @param position
   *          the position
   * @param storage
   *          the storage
   * @param restrictionSite
   *          the restriction site
   * @param used4sequencing
   *          the used4sequencing
   * @param used4qrtpcr
   *          the used4qrtpcr
   * @param used4nestedPcr
   *          the used4nested pcr
   * @param used4genotyping
   *          the used4genotyping
   * @param used4cloning
   *          the used4cloning
   * @param used4stdPcr
   *          the used4std pcr
   * @param referenceSeqDir
   *          the reference seq dir
   * @param referenceSeqFile
   *          the reference seq file
   * @param urllink
   *          the urllink
   * @param note
   *          the note
   * @param pcrsByPrimerReverseId
   *          the pcrs by primer reverse id
   * @param pcrsByPrimerForwardId
   *          the pcrs by primer forward id
   * @param peopleByPcrcieContactId
   *          the people by pcrcie contact id
   * @param peopleByDesignedById
   *          the people by designed by id
   * @param sequenceLength
   *          the sequence length
   * @param application
   *          the application
   * @param reference
   *          the reference
   * @param targetSpecies
   *          the target species
   * @param supplier
   *          the supplier
   * @param dateOrdered
   *          the date ordered
   * @param purification
   *          the purification
   * @param region
   *          the region
   * @param group
   *          the group
   * @param location
   *          the location
   * @param litReference
   *          the lit reference
   * @param designedBy
   *          the designed by
   * @param stockConcentration
   *          the stock concentration
   * @param lotNumber
   *          the lot number
   * @param dateDestroyed
   *          the date destroyed
   * @param pooledPrimer
   *          the pooled primer
   */
  @Builder
  public PcrPrimer(PrimerType type, String name, Integer version, String seq, String direction,
      String tmCalculated, Integer tmPe, String position, String storage, String restrictionSite,
      Boolean used4sequencing, Boolean used4qrtpcr, Boolean used4nestedPcr, Boolean used4genotyping,
      Boolean used4cloning, Boolean used4stdPcr, String referenceSeqDir, String referenceSeqFile,
      String urllink, String note,
      String sequenceLength, String application, String reference, String targetSpecies,
      String supplier, LocalDate dateOrdered, String purification, Region region, String litReference, String designedBy, String stockConcentration,
      Integer lotNumber, LocalDate dateDestroyed, PcrPrimer pooledPrimer, UUID uuid) {
    this.type = type;
    this.name = name;
    this.version = version;
    this.seq = seq;
    this.direction = direction;
    this.tmCalculated = tmCalculated;
    this.tmPe = tmPe;
    this.position = position;
    this.storage = storage;
    this.restrictionSite = restrictionSite;
    this.used4sequencing = used4sequencing;
    this.used4qrtpcr = used4qrtpcr;
    this.used4nestedPcr = used4nestedPcr;
    this.used4genotyping = used4genotyping;
    this.used4cloning = used4cloning;
    this.used4stdPcr = used4stdPcr;
    this.referenceSeqDir = referenceSeqDir;
    this.referenceSeqFile = referenceSeqFile;
    this.urllink = urllink;
    this.note = note;
    this.sequenceLength = sequenceLength;
    this.targetSpecies = targetSpecies;
    this.supplier = supplier;
    this.dateOrdered = dateOrdered;
    this.purification = purification;
    this.application = application;
    this.reference = reference;
    this.region = region;
    this.litReference = litReference;
    this.designedBy = designedBy;
    this.stockConcentration = stockConcentration;
    this.lotNumber = lotNumber;
    this.dateDestroyed = dateDestroyed;
    this.pooledPrimer = pooledPrimer;
    this.uuid = uuid;
  }

  // Property accessors
  /**
   * Gets the pcr primer id.
   *
   * @return the pcr primer id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PCRPrimerID")
  public Integer getPcrPrimerId() {
    return this.pcrPrimerId;
  }

  /**
   * Sets the pcr primer id.
   *
   * @param pcrprimerId
   *          the new pcr primer id
   */
  public void setPcrPrimerId(Integer pcrprimerId) {
    this.pcrPrimerId = pcrprimerId;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  @Column(name = "Type")
  @NotNull
  @Enumerated(EnumType.STRING)
  public PrimerType getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type
   *          the new type
   */
  public void setType(PrimerType type) {
    this.type = type;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @NotNull
  @Size(max = 191)
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
   * Gets the version.
   *
   * @return the version
   */
  @Column(name = "Version")
  public Integer getVersion() {
    return this.version;
  }

  /**
   * Sets the version.
   *
   * @param version
   *          the new version
   */
  public void setVersion(Integer version) {
    this.version = version;
  }

  /**
   * Gets the seq.
   *
   * @return the seq
   */
  @NotNull
  @Size(max = 255)
  @Column(name = "Seq", columnDefinition = "varchar(255) DEFAULT ''")
  public String getSeq() {
    return this.seq;
  }

  /**
   * Sets the seq.
   *
   * @param seq
   *          the new seq
   */
  public void setSeq(String seq) {
    this.seq = seq;
  }

  /**
   * Gets the direction.
   *
   * @return the direction
   */
  @Size(max = 1)
  @Pattern(regexp = "[FR]")
  @Column(name = "Direction")
  public String getDirection() {
    return this.direction;
  }

  /**
   * Sets the direction.
   *
   * @param direction
   *          the new direction
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Gets the tm calculated.
   *
   * @return the tm calculated
   */
  @Size(max = 11)
  @Column(name = "TmCalculated")
  public String getTmCalculated() {
    return this.tmCalculated;
  }

  /**
   * Sets the tm calculated.
   *
   * @param tmCalculated
   *          the new tm calculated
   */
  public void setTmCalculated(String tmCalculated) {
    this.tmCalculated = tmCalculated;
  }

  /**
   * Gets the tm pe.
   *
   * @return the tm pe
   */
  @Column(name = "TmPE")
  public Integer getTmPe() {
    return this.tmPe;
  }

  /**
   * Sets the tm pe.
   *
   * @param tmPe
   *          the new tm pe
   */
  public void setTmPe(Integer tmPe) {
    this.tmPe = tmPe;
  }

  /**
   * Gets the position.
   *
   * @return the position
   */
  @Size(max = 10)
  @Column(name = "Position")
  public String getPosition() {
    return this.position;
  }

  /**
   * Sets the position.
   *
   * @param position
   *          the new position
   */
  public void setPosition(String position) {
    this.position = position;
  }

  /**
   * Gets the storage.
   *
   * @return the storage
   */
  @Size(max = 50)
  @Column(name = "Storage")
  public String getStorage() {
    return this.storage;
  }

  /**
   * Sets the storage.
   *
   * @param storage
   *          the new storage
   */
  public void setStorage(String storage) {
    this.storage = storage;
  }

  /**
   * Gets the restriction site.
   *
   * @return the restriction site
   */
  @Column(name = "RestrictionSite")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
  public String getRestrictionSite() {
    return this.restrictionSite;
  }

  /**
   * Sets the restriction site.
   *
   * @param restrictionSite
   *          the new restriction site
   */
  public void setRestrictionSite(String restrictionSite) {
    this.restrictionSite = restrictionSite;
  }

  /**
   * Gets the used4sequencing.
   *
   * @return the used4sequencing
   */
  @Column(name = "Used4Sequencing")
  public Boolean getUsed4sequencing() {
    return this.used4sequencing;
  }

  /**
   * Sets the used4sequencing.
   *
   * @param used4sequencing
   *          the new used4sequencing
   */
  public void setUsed4sequencing(Boolean used4sequencing) {
    this.used4sequencing = used4sequencing;
  }

  /**
   * Gets the used4qrtpcr.
   *
   * @return the used4qrtpcr
   */
  @Column(name = "Used4QRTPCR")
  public Boolean getUsed4qrtpcr() {
    return this.used4qrtpcr;
  }

  /**
   * Sets the used4qrtpcr.
   *
   * @param used4qrtpcr
   *          the new used4qrtpcr
   */
  public void setUsed4qrtpcr(Boolean used4qrtpcr) {
    this.used4qrtpcr = used4qrtpcr;
  }

  /**
   * Gets the used4nested pcr.
   *
   * @return the used4nested pcr
   */
  @Column(name = "Used4NestedPCR")
  public Boolean getUsed4nestedPcr() {
    return this.used4nestedPcr;
  }

  /**
   * Sets the used4nested pcr.
   *
   * @param used4nestedPcr
   *          the new used4nested pcr
   */
  public void setUsed4nestedPcr(Boolean used4nestedPcr) {
    this.used4nestedPcr = used4nestedPcr;
  }

  /**
   * Gets the used4genotyping.
   *
   * @return the used4genotyping
   */
  @Column(name = "Used4Genotyping")
  public Boolean getUsed4genotyping() {
    return this.used4genotyping;
  }

  /**
   * Sets the used4genotyping.
   *
   * @param used4genotyping
   *          the new used4genotyping
   */
  public void setUsed4genotyping(Boolean used4genotyping) {
    this.used4genotyping = used4genotyping;
  }

  /**
   * Gets the used4cloning.
   *
   * @return the used4cloning
   */
  @Column(name = "Used4Cloning")
  public Boolean getUsed4cloning() {
    return this.used4cloning;
  }

  /**
   * Sets the used4cloning.
   *
   * @param used4cloning
   *          the new used4cloning
   */
  public void setUsed4cloning(Boolean used4cloning) {
    this.used4cloning = used4cloning;
  }

  /**
   * Gets the used4std pcr.
   *
   * @return the used4std pcr
   */
  @Column(name = "Used4StdPCR")
  public Boolean getUsed4stdPcr() {
    return this.used4stdPcr;
  }

  /**
   * Sets the used4std pcr.
   *
   * @param used4stdPcr
   *          the new used4std pcr
   */
  public void setUsed4stdPcr(Boolean used4stdPcr) {
    this.used4stdPcr = used4stdPcr;
  }

  /**
   * Gets the reference seq dir.
   *
   * @return the reference seq dir
   */
  @Column(name = "ReferenceSeqDir")
  public String getReferenceSeqDir() {
    return this.referenceSeqDir;
  }

  /**
   * Sets the reference seq dir.
   *
   * @param referenceSeqDir
   *          the new reference seq dir
   */
  public void setReferenceSeqDir(String referenceSeqDir) {
    this.referenceSeqDir = referenceSeqDir;
  }

  /**
   * Gets the reference seq file.
   *
   * @return the reference seq file
   */
  @Column(name = "ReferenceSeqFile")
  public String getReferenceSeqFile() {
    return this.referenceSeqFile;
  }

  /**
   * Sets the reference seq file.
   *
   * @param referenceSeqFile
   *          the new reference seq file
   */
  public void setReferenceSeqFile(String referenceSeqFile) {
    this.referenceSeqFile = referenceSeqFile;
  }

  /**
   * Gets the urllink.
   *
   * @return the urllink
   */
  @Size(max = 50)
  @Column(name = "URLLink")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
  public String getUrllink() {
    return this.urllink;
  }

  /**
   * Sets the urllink.
   *
   * @param urllink
   *          the new urllink
   */
  public void setUrllink(String urllink) {
    this.urllink = urllink;
  }

  /**
   * Gets the note.
   *
   * @return the note
   */
  @Size(max = 1024)
  @Column(name = "Note")
  public String getNote() {
    return this.note;
  }

  /**
   * Sets the note.
   *
   * @param note
   *          the new note
   */
  public void setNote(String note) {
    this.note = note;
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

  /**
   * Sets the last modified.
   *
   * @param lastModified
   *          the new last modified
   */
  public void setLastModified(Timestamp lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Gets the sequence length.
   *
   * @return the sequence length
   */
  @Size(max = 50)
  @Pattern(regexp = "\\d+")
  @Column(name = "SequenceLength")
  public String getSequenceLength() {
    return sequenceLength;
  }

  /**
   * Sets the sequence length.
   *
   * @param sequenceLength
   *          the new sequence length
   */
  public void setSequenceLength(String sequenceLength) {
    this.sequenceLength = sequenceLength;
  }

  /**
   * Gets the target species.
   *
   * @return the target species
   */
  @Size(max = 50)
  @Column(name = "TargetSpecies")
  public String getTargetSpecies() {
    return targetSpecies;
  }

  /**
   * Sets the target species.
   *
   * @param targetSpecies
   *          the new target species
   */
  public void setTargetSpecies(String targetSpecies) {
    this.targetSpecies = targetSpecies;
  }

  /**
   * Gets the supplier.
   *
   * @return the supplier
   */
  @Size(max = 50)
  @Column(name = "Supplier")
  public String getSupplier() {
    return supplier;
  }

  /**
   * Sets the supplier.
   *
   * @param supplier
   *          the new supplier
   */
  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }

  /**
   * Gets the date ordered.
   *
   * @return the date ordered
   */
  @Column(name = "DateOrdered")
  public LocalDate getDateOrdered() {
    return dateOrdered;
  }

  /**
   * Sets the date ordered.
   *
   * @param dateOrdered
   *          the new date ordered
   */
  public void setDateOrdered(LocalDate dateOrdered) {
    this.dateOrdered = dateOrdered;
  }

  /**
   * Gets the purification.
   *
   * @return the purification
   */
  @Size(max = 50)
  @Column(name = "Purification")
  public String getPurification() {
    return purification;
  }

  /**
   * Sets the purification.
   *
   * @param purification
   *          the new purification
   */
  public void setPurification(String purification) {
    this.purification = purification;
  }

  /**
   * Gets the application.
   *
   * @return the application
   */
  @Size(max = 200)
  @Column(name = "Application")
  public String getApplication() {
    return application;
  }

  /**
   * Sets the application.
   *
   * @param application
   *          the new application
   */
  public void setApplication(String application) {
    this.application = application;
  }

  /**
   * Gets the reference.
   *
   * @return the reference
   */
  @Column(name = "Reference")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
  public String getReference() {
    return this.reference;
  }

  /**
   * Sets the reference.
   *
   * @param reference
   *          the new reference
   */
  public void setReference(String reference) {
    this.reference = reference;
  }

  /**
   * Gets the region.
   *
   * @return the region
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "RegionID")
  public Region getRegion() {
    return region;
  }

  /**
   * Sets the region.
   *
   * @param region
   *          the new region
   */
  public void setRegion(Region region) {
    this.region = region;
  }

  /**
   * Gets the pooled primer.
   *
   * @return the pooled primer.
   */

  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PooledPrimerID", unique = false, nullable = true, insertable = true, updatable = true)
  public PcrPrimer getPooledPrimer() {
    return this.pooledPrimer;
  }

  /**
   * Sets the pooled primer.
   *
   * @param pooledPrimer
   *          the new pooled primer
   */
  public void setPooledPrimer(PcrPrimer pooledPrimer) {
    this.pooledPrimer = pooledPrimer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getUniqueId()
   */
  @Transient
  public Integer getId() {
    return getPcrPrimerId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setPcrPrimerId(id);
  }

  /**
   * Gets the lit reference.
   *
   * @return the lit reference
   */
  @Size(max = 512)
  @Column(name = "LitReference")
  public String getLitReference() {
    return litReference;
  }

  /**
   * Sets the lit reference.
   *
   * @param litReference
   *          the new lit reference
   */
  public void setLitReference(String litReference) {
    this.litReference = litReference;
  }

  /**
   * Gets the designed By.
   *
   * @return the designed By
   */
  @Size(max = 50)
  @Column(name = "DesignedBy")
  public String getDesignedBy() {
    return designedBy;
  }

  /**
   * Sets the designed By.
   *
   * @param designedBy
   *          the designedBy
   */
  public void setDesignedBy(String designedBy) {
    this.designedBy = designedBy;
  }

  /**
   * Gets the stock concentration.
   *
   * @return the stock concentration
   */
  @Size(max = 10)
  @Column(name = "StockConcentration")
  public String getStockConcentration() {
    return stockConcentration;
  }

  /**
   * Sets the stock concentration.
   *
   * @param stockConcentration
   *          the new stock concentration
   */
  public void setStockConcentration(String stockConcentration) {
    this.stockConcentration = stockConcentration;
  }

  /**
   * Gets the lot number.
   *
   * @return the lot number
   */
  @NotNull
  @Column(name = "LotNumber")
  public Integer getLotNumber() {
    return lotNumber;
  }

  /**
   * Sets the lot number.
   *
   * @param lotNumber
   *          the new lot number
   */
  public void setLotNumber(Integer lotNumber) {
    this.lotNumber = lotNumber;
  }

  /**
   * Gets the date destroyed
   *
   * @return the date destroyed
   */
  @Column(name = "DateDestroyed")
  public LocalDate getDateDestroyed() {
    return dateDestroyed;
  }

  /**
   * Sets the date destroyed.
   *
   * @param dateDestroyed
   *          the new date destroyed
   */
  public void setDateDestroyed(LocalDate dateDestroyed) {
    this.dateDestroyed = dateDestroyed;
  }

  /**
   * Get the UUID
   * @return the UUID
   */
  @Column(name = "uuid", unique = true)
  @NotNull
  public UUID getUUID() {
    return uuid;
  }

  /**
   * Sets the UUID
   * @param UUID
   */
  public void setUUID(UUID uuid) {
    this.uuid = uuid;
  }

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }
}
