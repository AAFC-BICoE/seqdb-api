/*
 * =====================================================================
 * Class:		PcrBatch.java
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

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;





import lombok.Builder;

/**
 * The Class PcrBatch.
 */
@Entity

@Table(name = "PcrBatchs", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "Name", "GroupID" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class PcrBatch implements  RestrictedByGroup {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 5934508588429961752L;

  /**
   * The Enum PcrBatchType.
   */
  public enum PcrBatchType {

    /** The sanger. */
    SANGER("Sanger"),

    /** The ngs. */
    NGS("Illumina (NGS)"),

    /** The round2. */
    ROUND2("Round 2"),

    /** PCR with no intention of sending it for sequencing */
    FRAGMENT("Fragment");

    /** The value. */
    private final String value;

    /**
     * Instantiates a new protocol type.
     *
     * @param value
     *          the value
     */
    PcrBatchType(String value) {
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

  /**
   * The Enum PcrBatchPlateSize.
   */
  public enum PcrBatchPlateSize {

    /** Plate number 96 and 8 is wells (default) */
    PLATE_NUMBER_96(96, 8),

    /** Plate number is 384 and 16 is rows number. */
    PLATE_NUMBER_384(384, 16);

    /** The value. */
    private final int wells;
    private final int rows;

    /**
     * Instantiates a new plate size.
     *
     * @param wells
     *          the wells
     * @param rows
     *          the rows
     */
    PcrBatchPlateSize(int wells, int rows) {
      this.wells = wells;
      this.rows = rows;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getWells() {
      return wells;
    }

    public int getRows() {
      return rows;
    }

    public static Optional<PcrBatchPlateSize> fromWells(int wells) {
      for (PcrBatchPlateSize plateSize : PcrBatchPlateSize.values()) {
        if (plateSize.wells == wells) {
          return Optional.of(plateSize);
        }
      }
      return Optional.empty();
    }

  }

  /** The pcr batch id. */
  private Integer pcrBatchId;

  /** The type. */
  private PcrBatchType type;

  /** The plate size. */
  private PcrBatchPlateSize plateSize;

  /** The name. */
  private String name;

  /** The project. */
  private String project;

  /** The experimenter. */
  private String experimenter;

  /** The reaction date. */
  private Date reactionDate;

  /** The thermocycler. */
  private String thermocycler;

  /** The objective. */
  private String objective;

  /** The reference. */
  private String reference;

  /** The positive control. */
  private String positiveControl;

  /** The reaction volume. */
  private String reactionVolume;

  /** The result. */
  private String result;

  /** The purification. */
  private String purification;

  /** The pcr pooling number. */
  private String pcrPoolingNumber;

  /** The dna dilution. */
  private String dnaDilution;

  /** The quantification method. */
  private String quantificationMethod;

  /** The polymerase. */
  private String polymerase;

  /** The clean up method. */
  private String cleanUpMethod;

  /** The clean up plate id. */
  private String cleanUpPlateId;

  /** The clean up date. */
  private Date cleanUpDate;

  /** The normalization method. */
  private String normalizationMethod;

  /** The normalized concentration. */
  private String normalizedConcentration;

  /** The normalization plate id. */
  private String normalizationPlateId;

  /** The notes. */
  private String notes;

  /** The last modified. */
  private Timestamp lastModified;

  /** The pooled. */
  private Boolean pooled;

  /** The region. */
  private Region region;

  /** The primer forward. */
  private PcrPrimer primerForward;

  /** The primer reverse. */
  private PcrPrimer primerReverse;

  /** The pcr profile. */
  private PcrProfile pcrProfile;

  /** The group. */
  private Group group;

  /** The protocol. */
  private Protocol protocol;

  /** The round 2 batch. */
  private PcrBatch round2Batch;

  /** The reactions. */
  private List<PcrReaction> reactions = new ArrayList<PcrReaction>();

  /** Container type of this batch's plate */
  private ContainerType containerType;

  /**
   * Instantiates a new pcr batch.
   */
  public PcrBatch() {
  }

  /**
   * Instantiates a new pcr batch.
   *
   * @param name
   *          the name
   * @param group
   *          the group
   */
  public PcrBatch(String name, Group group, ContainerType containerType) {
    this.name = name;
    this.group = group;
    this.containerType = containerType;
  }

  /**
   * Instantiates a new pcr batch.
   *
   * @param type
   *          the type
   * @param name
   *          the name
   * @param experimenter
   *          the experimenter
   * @param reactionDate
   *          the reaction date
   * @param thermocycler
   *          the thermocycler
   * @param objective
   *          the objective
   * @param reference
   *          the reference
   * @param positiveControl
   *          the positive control
   * @param reactionVolume
   *          the reaction volume
   * @param result
   *          the result
   * @param purification
   *          the purification
   * @param pcrPoolingNumber
   *          the pcr pooling number
   * @param dnaDilution
   *          the dna dilution
   * @param quantificationMethod
   *          the quantification method
   * @param polymerase
   *          the polymerase
   * @param cleanUpMethod
   *          the clean up method
   * @param cleanUpPlateId
   *          the clean up plate id
   * @param cleanUpDate
   *          the clean up date
   * @param normalizationMethod
   *          the normalization method
   * @param normalizedConcentration
   *          the normalized concentration
   * @param normalizationPlateId
   *          the normalization plate id
   * @param notes
   *          the notes
   * @param region
   *          the region
   * @param primerForward
   *          the primer forward
   * @param primerReverse
   *          the primer reverse
   * @param pcrProfile
   *          the pcr profile
   * @param group
   *          the group
   * @param pooled
   *          the pooled
   * @param protocol
   *          the protocol
   * @param round2Batch
   *          the round 2 batch
   * @param containerType
   *          container type has information on the plate
   */
  @Builder
  public PcrBatch(PcrBatchType type, String name, String experimenter, Date reactionDate,
      String thermocycler, String objective, String reference, String positiveControl,
      String reactionVolume, String result, String purification, String pcrPoolingNumber,
      String dnaDilution, String quantificationMethod, String polymerase, String cleanUpMethod,
      String cleanUpPlateId, Date cleanUpDate, String normalizationMethod,
      String normalizedConcentration, String normalizationPlateId, String notes, Region region,
      PcrPrimer primerForward, PcrPrimer primerReverse, PcrProfile pcrProfile, Group group,
      Boolean pooled, Protocol protocol, PcrBatch round2Batch, ContainerType containerType) {
    super();
    this.type = type;
    this.name = name;
    this.experimenter = experimenter;
    this.reactionDate = reactionDate;
    this.thermocycler = thermocycler;
    this.objective = objective;
    this.reference = reference;
    this.positiveControl = positiveControl;
    this.reactionVolume = reactionVolume;
    this.result = result;
    this.purification = purification;
    this.pcrPoolingNumber = pcrPoolingNumber;
    this.dnaDilution = dnaDilution;
    this.quantificationMethod = quantificationMethod;
    this.polymerase = polymerase;
    this.cleanUpMethod = cleanUpMethod;
    this.cleanUpPlateId = cleanUpPlateId;
    this.cleanUpDate = cleanUpDate;
    this.normalizationMethod = normalizationMethod;
    this.normalizedConcentration = normalizedConcentration;
    this.normalizationPlateId = normalizationPlateId;
    this.notes = notes;
    this.region = region;
    this.primerForward = primerForward;
    this.primerReverse = primerReverse;
    this.pcrProfile = pcrProfile;
    this.group = group;
    this.pooled = pooled;
    this.protocol = protocol;
    this.round2Batch = round2Batch;
    this.containerType = containerType;
  }

  // Property accessors
  /**
   * Gets the pcr batch id.
   *
   * @return the pcr batch id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PCRBatchID")
  public Integer getPcrBatchId() {
    return pcrBatchId;
  }

  /**
   * Sets the pcr batch id.
   *
   * @param pcrBatchId
   *          the new pcr batch id
   */
  public void setPcrBatchId(Integer pcrBatchId) {
    this.pcrBatchId = pcrBatchId;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  @NotNull
  @Column(name = "Type")
  @Enumerated(EnumType.STRING)
  public PcrBatchType getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type
   *          the new type
   */
  public void setType(PcrBatchType type) {
    this.type = type;
  }

  /**
   * Gets the plate size.
   *
   * @return the plate size
   */
  @Transient
  public PcrBatchPlateSize getPlateSize() {
    return plateSize;
  }

  /**
   * Sets the plate size.
   *
   * @param plateSize
   *          the new plate size
   */
  public void setPlateSize(PcrBatchPlateSize plateSize) {
    this.plateSize = plateSize;
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
   * Gets the project.
   *
   * @return the project
   */
  @Size(max = 50)
  @Column(name = "Project")
  public String getProject() {
    return this.project;
  }

  /**
   * Sets the project.
   *
   * @param project
   *          the new project
   */
  public void setProject(String project) {
    this.project = project;
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
   * Gets the thermocycler.
   *
   * @return the thermocycler
   */
  @Size(max = 50)
  @Column(name = "Thermocycler")
  public String getThermocycler() {
    return thermocycler;
  }

  /**
   * Sets the thermocycler.
   *
   * @param thermocycler
   *          the new thermocycler
   */
  public void setThermocycler(String thermocycler) {
    this.thermocycler = thermocycler;
  }

  /**
   * Gets the reaction date.
   *
   * @return the reaction date
   */
  @Column(name = "ReactionDate")
  public Date getReactionDate() {
    return reactionDate;
  }

  /**
   * Sets the reaction date.
   *
   * @param reactionDate
   *          the new reaction date
   */
  public void setReactionDate(Date reactionDate) {
    this.reactionDate = reactionDate;
  }

  /**
   * Gets the reaction volume.
   *
   * @return the reaction volume
   */
  @Size(max = 50)
  @Column(name = "ReactionVolume")
  public String getReactionVolume() {
    return reactionVolume;
  }

  /**
   * Sets the reaction volume.
   *
   * @param reactionVolume
   *          the new reaction volume
   */
  public void setReactionVolume(String reactionVolume) {
    this.reactionVolume = reactionVolume;
  }

  /**
   * Gets the objective.
   *
   * @return the objective
   */
  @Size(max = 200)
  @Column(name = "Objective")
  public String getObjective() {
    return objective;
  }

  /**
   * Sets the objective.
   *
   * @param objective
   *          the new objective
   */
  public void setObjective(String objective) {
    this.objective = objective;
  }

  /**
   * Gets the reference.
   *
   * @return the reference
   */
  @Size(max = 200)
  @Column(name = "Reference")
  public String getReference() {
    return reference;
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
   * Gets the result.
   *
   * @return the result
   */
  @Size(max = 200)
  @Column(name = "Result")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
  public String getResult() {
    return this.result;
  }

  /**
   * Sets the result.
   *
   * @param result
   *          the new result
   */
  public void setResult(String result) {
    this.result = result;
  }

  /**
   * Gets the positive control.
   *
   * @return the positive control
   */
  @Size(max = 50)
  @Column(name = "PositiveControl")
  public String getPositiveControl() {
    return positiveControl;
  }

  /**
   * Sets the positive control.
   *
   * @param positiveControl
   *          the new positive control
   */
  public void setPositiveControl(String positiveControl) {
    this.positiveControl = positiveControl;
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
   * Gets the pcr pooling number.
   *
   * @return the pcr pooling number
   */
  @Size(max = 50)
  @Column(name = "PcrPoolingNumber")
  public String getPcrPoolingNumber() {
    return pcrPoolingNumber;
  }

  /**
   * Sets the pcr pooling number.
   *
   * @param pcrPoolingNumber
   *          the new pcr pooling number
   */
  public void setPcrPoolingNumber(String pcrPoolingNumber) {
    this.pcrPoolingNumber = pcrPoolingNumber;
  }

  /**
   * Gets the dna dilution.
   *
   * @return the dna dilution
   */
  @Size(max = 50)
  @Column(name = "DnaDilution")
  public String getDnaDilution() {
    return dnaDilution;
  }

  /**
   * Sets the dna dilution.
   *
   * @param dnaDilution
   *          the new dna dilution
   */
  public void setDnaDilution(String dnaDilution) {
    this.dnaDilution = dnaDilution;
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
   * Gets the polymerase.
   *
   * @return the polymerase
   */
  @Size(max = 50)
  @Column(name = "Polymerase")
  public String getPolymerase() {
    return polymerase;
  }

  /**
   * Sets the polymerase.
   *
   * @param polymerase
   *          the new polymerase
   */
  public void setPolymerase(String polymerase) {
    this.polymerase = polymerase;
  }

  /**
   * Gets the clean up method.
   *
   * @return the clean up method
   */
  @Size(max = 100)
  @Column(name = "CleanUpMethod")
  public String getCleanUpMethod() {
    return cleanUpMethod;
  }

  /**
   * Sets the clean up method.
   *
   * @param cleanUpMethod
   *          the new clean up method
   */
  public void setCleanUpMethod(String cleanUpMethod) {
    this.cleanUpMethod = cleanUpMethod;
  }

  /**
   * Gets the clean up plate id.
   *
   * @return the clean up plate id
   */
  @Size(max = 50)
  @Column(name = "CleanUpPlateId")
  public String getCleanUpPlateId() {
    return cleanUpPlateId;
  }

  /**
   * Sets the clean up plate id.
   *
   * @param cleanUpPlateId
   *          the new clean up plate id
   */
  public void setCleanUpPlateId(String cleanUpPlateId) {
    this.cleanUpPlateId = cleanUpPlateId;
  }

  /**
   * Gets the clean up date.
   *
   * @return the clean up date
   */
  @Column(name = "CleanUpDate")
  public Date getCleanUpDate() {
    return cleanUpDate;
  }

  /**
   * Sets the clean up date.
   *
   * @param cleanUpDate
   *          the new clean up date
   */
  public void setCleanUpDate(Date cleanUpDate) {
    this.cleanUpDate = cleanUpDate;
  }

  /**
   * Gets the normalization method.
   *
   * @return the normalization method
   */
  @Size(max = 100)
  @Column(name = "NormalizationMethod")
  public String getNormalizationMethod() {
    return normalizationMethod;
  }

  /**
   * Sets the normalization method.
   *
   * @param normalizationMethod
   *          the new normalization method
   */
  public void setNormalizationMethod(String normalizationMethod) {
    this.normalizationMethod = normalizationMethod;
  }

  /**
   * Gets the normalized concentration.
   *
   * @return the normalized concentration
   */
  @Size(max = 20)
  @Column(name = "NormalizedConcentration")
  public String getNormalizedConcentration() {
    return normalizedConcentration;
  }

  /**
   * Sets the normalized concentration.
   *
   * @param normalizedConcentration
   *          the new normalized concentration
   */
  public void setNormalizedConcentration(String normalizedConcentration) {
    this.normalizedConcentration = normalizedConcentration;
  }

  /**
   * Gets the normalization plate id.
   *
   * @return the normalization plate id
   */
  @Size(max = 50)
  @Column(name = "NormalizationPlateId")
  public String getNormalizationPlateId() {
    return normalizationPlateId;
  }

  /**
   * Sets the normalization plate id.
   *
   * @param normalizationPlateId
   *          the new normalization plate id
   */
  public void setNormalizationPlateId(String normalizationPlateId) {
    this.normalizationPlateId = normalizationPlateId;
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
    return this.notes;
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

  /**
   * Gets the primer reverse.
   *
   * @return the primer reverse
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PrimerReverseID")
  public PcrPrimer getPrimerReverse() {
    return this.primerReverse;
  }

  /**
   * Sets the primer reverse.
   *
   * @param reversePrimer
   *          the reverse primer
   */
  public void setPrimerReverse(PcrPrimer reversePrimer) {
    this.primerReverse = reversePrimer;
  }

  /**
   * Gets the primer forward.
   *
   * @return the primer forward
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PrimerForwardID")
  public PcrPrimer getPrimerForward() {
    return this.primerForward;
  }

  /**
   * Sets the primer forward.
   *
   * @param forwardPrimer
   *          the new primer forward
   */
  public void setPrimerForward(PcrPrimer forwardPrimer) {
    this.primerForward = forwardPrimer;
  }

  /**
   * Gets the pcr profile.
   *
   * @return the pcr profile
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PCRProfileID")
  public PcrProfile getPcrProfile() {
    return pcrProfile;
  }

  /**
   * Sets the pcr profile.
   *
   * @param pcrProfile
   *          the new pcr profile
   */
  public void setPcrProfile(PcrProfile pcrProfile) {
    this.pcrProfile = pcrProfile;
  }

  /**
   * Gets the region.
   *
   * @return the seq source
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
   * Gets the group.
   *
   * @return the group
   */
  
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "GroupID")
  public Group getGroup() {
    return this.group;
  }

  /**
   * Sets the group.
   *
   * @param group
   *          the new group
   */
  
  public void setGroup(Group group) {
    this.group = group;
  }

  /**
   * Gets the reactions.
   *
   * @return the reactions
   */
  @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "pcrBatch")
  @OrderBy("PcrReactionID")
  public List<PcrReaction> getReactions() {
    return reactions;
  }

  /**
   * Sets the reactions.
   *
   * @param reactions
   *          the new reactions
   */
  public void setReactions(List<PcrReaction> reactions) {
    this.reactions = reactions;
  }

  /**
   * Gets the pooled.
   *
   * @return the pooled
   */
  @Column(name = "Pooled")
  public Boolean getPooled() {
    return pooled;
  }

  /**
   * Sets the pooled.
   *
   * @param pooled
   *          the new pooled
   */
  public void setPooled(Boolean pooled) {
    this.pooled = pooled;
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

  /**
   * Gets the round 2 batch.
   *
   * @return the round 2 batch
   */
  @OneToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "Round2BatchID")
  public PcrBatch getRound2Batch() {
    return round2Batch;
  }

  /**
   * Sets the round 2 batch.
   *
   * @param round2Batch
   *          the new round 2 batch
   */
  public void setRound2Batch(PcrBatch round2Batch) {
    this.round2Batch = round2Batch;
  }

  /**
   * Gets container type of this batch
   * 
   * @return ContainerType associated with this batch
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "ContainerTypeID")
  public ContainerType getContainerType() {
    return this.containerType;
  }

  /**
   * Sets the container type of this batch
   * 
   * @param containerType
   *          the new containerType
   */
  public void setContainerType(ContainerType containerType) {
    this.containerType = containerType;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getUniqueId()
   */
  @Transient
  public Integer getId() {
    return getPcrBatchId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setPcrBatchId(id);
  }

  @Override
  @Transient
  @JsonIgnore
  public Group getAccessGroup() {
    return getGroup();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return super.toString() + ": Id=" + pcrBatchId + " / Name=" + name + " / GroupId="
        + (group != null ? group.getGroupId() : "null") + " / LastModified=" + lastModified;
  }

}
