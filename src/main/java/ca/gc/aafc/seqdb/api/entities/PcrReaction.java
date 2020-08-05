/*
 * =====================================================================
 * Class:		PcrReaction.java
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
 * The Class PcrReaction.
 */
@Entity

@Table(name = "PcrReactions", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "TubeNumber", "PCRBatchID", "GroupID" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class PcrReaction implements RestrictedByGroup {

  /** The pcr reaction id. */
  private Integer pcrReactionId;

  /** The result. */
  private String result;

  /** The note. */
  private String note;

  /** The loc start. */
  private Integer locStart;

  /** The loc end. */
  private Integer locEnd;

  /** The image dir. */
  private String imageDir;

  /** The image name. */
  private String imageName;

  /** The target. */
  private String target;

  /** The final concentration. */
  private String finalConcentration;

  /** The final quality. */
  private String finalQuality;

  /** The clean up plate coordinates. */
  private String cleanUpPlateCoordinates;

  /** The normalization coordinates. */
  private String normalizationCoordinates;

  /** The index set. */
  private String indexSet;

  /** The last modified. */
  private Timestamp lastModified;

  /** The pcr batch. */
  private PcrBatch pcrBatch;

  /** The mid. */
  private PcrPrimer mid;

  /** The sample. */
  private Sample sample;

  /** The group. */
  private Group group;

  /** The primer forward. */
  private PcrPrimer primerForward;

  /** The primer reverse. */
  private PcrPrimer primerReverse;

  /** The region. */
  private Region region;

  /** The round 1 reaction. */
  private PcrReaction round1Reaction;

  private Integer tubeNumber;

  // Constructors

  /**
   * Instantiates a new pcr reaction.
   */
  public PcrReaction() {
  }

  /**
   * Instantiates a new pcr reaction.
   *
   * @param tubeNumber
   *          the tubeNumber
   * @param group
   *          the group
   */
  public PcrReaction(Integer tubeNumber, Group group) {
    this.tubeNumber = tubeNumber;
    this.group = group;
  }

  /**
   * Instantiates a new pcr reaction.
   *
   * @param result
   *          the result
   * @param note
   *          the note
   * @param locStart
   *          the loc start
   * @param locEnd
   *          the loc end
   * @param imageDir
   *          the image dir
   * @param imageName
   *          the image name
   * @param target
   *          the target
   * @param finalConcentration
   *          the final concentration
   * @param finalQuality
   *          the final quality
   * @param cleanUpPlateCoordinates
   *          the clean up plate coordinates
   * @param normalizationCoordinates
   *          the normalization coordinates
   * @param indexSet
   *          the index set
   * @param pcrBatch
   *          the pcr batch
   * @param mid
   *          the mid
   * @param sample
   *          the sample
   * @param group
   *          the group
   * @param primerForward
   *          the primer forward
   * @param primerReverse
   *          the primer reverse
   * @param region
   *          the region
   * @param round1Reaction
   *          the round 1 reaction
   */
  @Builder
  public PcrReaction(String result, String note, Integer locStart, Integer locEnd, String imageDir,
      String imageName, String target, String finalConcentration, String finalQuality,
      String cleanUpPlateCoordinates, String normalizationCoordinates, String indexSet,
      PcrBatch pcrBatch, PcrPrimer mid, Sample sample, Group group, PcrPrimer primerForward,
      PcrPrimer primerReverse, Region region, PcrReaction round1Reaction, Integer tubeNumber) {
    super();
    this.result = result;
    this.note = note;
    this.locStart = locStart;
    this.locEnd = locEnd;
    this.imageDir = imageDir;
    this.imageName = imageName;
    this.target = target;
    this.finalConcentration = finalConcentration;
    this.finalQuality = finalQuality;
    this.cleanUpPlateCoordinates = cleanUpPlateCoordinates;
    this.normalizationCoordinates = normalizationCoordinates;
    this.indexSet = indexSet;
    this.pcrBatch = pcrBatch;
    this.mid = mid;
    this.sample = sample;
    this.group = group;
    this.primerForward = primerForward;
    this.primerReverse = primerReverse;
    this.region = region;
    this.round1Reaction = round1Reaction;
    this.tubeNumber = tubeNumber;
  }

  // Property accessors
  /**
   * Gets the pcr reaction id.
   *
   * @return the pcr reaction id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PcrReactionID")
  public Integer getPcrReactionId() {
    return this.pcrReactionId;
  }

  /**
   * Sets the pcr reaction id.
   *
   * @param pcrReactionId
   *          the new pcr reaction id
   */
  public void setPcrReactionId(Integer pcrReactionId) {
    this.pcrReactionId = pcrReactionId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getId()
   */
  @Transient
  public Integer getId() {
    return this.getPcrReactionId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setId(java.lang.Integer)
   */
  public void setId(Integer id) {
    this.setPcrReactionId(id);
  }

  /**
   * Gets the loc start.
   *
   * @return the loc start
   */
  @Column(name = "LocStart")
  public Integer getLocStart() {
    return this.locStart;
  }

  /**
   * Sets the loc start.
   *
   * @param locStart
   *          the new loc start
   */
  public void setLocStart(Integer locStart) {
    this.locStart = locStart;
  }

  /**
   * Gets the loc end.
   *
   * @return the loc end
   */
  @Column(name = "LocEnd")
  public Integer getLocEnd() {
    return this.locEnd;
  }

  /**
   * Sets the loc end.
   *
   * @param locEnd
   *          the new loc end
   */
  public void setLocEnd(Integer locEnd) {
    this.locEnd = locEnd;
  }

  /**
   * Gets the result.
   *
   * @return the result
   */
  @Size(max = 50)
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
   * Gets the note.
   *
   * @return the note
   */
  @Size(max = 1024)
  @Column(name = "Note")
  @Lob
  // https://stackoverflow.com/questions/12647755/bad-value-for-type-long-postgresql-hibernate-spring#answer-21546893
  @Type(type = "org.hibernate.type.TextType")
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
   * Gets the image dir.
   *
   * @return the image dir
   */
  @Column(name = "ImageDir")
  public String getImageDir() {
    return this.imageDir;
  }

  /**
   * Sets the image dir.
   *
   * @param imageDir
   *          the new image dir
   */
  public void setImageDir(String imageDir) {
    this.imageDir = imageDir;
  }

  /**
   * Gets the image name.
   *
   * @return the image name
   */
  @Size(max = 50)
  @Column(name = "ImageName")
  public String getImageName() {
    return this.imageName;
  }

  /**
   * Sets the image name.
   *
   * @param imageName
   *          the new image name
   */
  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  /**
   * Gets the target.
   *
   * @return the target
   */
  @Size(max = 50)
  @Column(name = "Target")
  public String getTarget() {
    return this.target;
  }

  /**
   * Sets the target.
   *
   * @param target
   *          the new target
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Gets the final concentration.
   *
   * @return the final concentration
   */
  @Size(max = 50)
  @Column(name = "FinalConcentration")
  public String getFinalConcentration() {
    return finalConcentration;
  }

  /**
   * Sets the final concentration.
   *
   * @param finalConcentration
   *          the new final concentration
   */
  public void setFinalConcentration(String finalConcentration) {
    this.finalConcentration = finalConcentration;
  }

  /**
   * Gets the final quality.
   *
   * @return the final quality
   */
  @Size(max = 50)
  @Column(name = "FinalQuality")
  public String getFinalQuality() {
    return finalQuality;
  }

  /**
   * Sets the final quality.
   *
   * @param finalQuality
   *          the new final quality
   */
  public void setFinalQuality(String finalQuality) {
    this.finalQuality = finalQuality;
  }

  /**
   * Gets the clean up plate coordinates.
   *
   * @return the clean up plate coordinates
   */
  @Size(max = 5)
  @Column(name = "CleanUpPlateCoordinates")
  public String getCleanUpPlateCoordinates() {
    return cleanUpPlateCoordinates;
  }

  /**
   * Sets the clean up plate coordinates.
   *
   * @param cleanUpPlateCoordinates
   *          the new clean up plate coordinates
   */
  public void setCleanUpPlateCoordinates(String cleanUpPlateCoordinates) {
    this.cleanUpPlateCoordinates = cleanUpPlateCoordinates;
  }

  /**
   * Gets the normalization coordinates.
   *
   * @return the normalization coordinates
   */
  @Size(max = 5)
  @Column(name = "NormalizationCoordinates")
  public String getNormalizationCoordinates() {
    return normalizationCoordinates;
  }

  /**
   * Sets the normalization coordinates.
   *
   * @param normalizationCoordinates
   *          the new normalization coordinates
   */
  public void setNormalizationCoordinates(String normalizationCoordinates) {
    this.normalizationCoordinates = normalizationCoordinates;
  }

  /**
   * Gets the index set.
   *
   * @return the index set
   */
  @Size(max = 50)
  @Column(name = "IndexSet")
  public String getIndexSet() {
    return indexSet;
  }

  /**
   * Sets the index set.
   *
   * @param indexSet
   *          the new index set
   */
  public void setIndexSet(String indexSet) {
    this.indexSet = indexSet;
  }

  /**
   * Gets the sample.
   *
   * @return the sample
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "SampleID")
  public Sample getSample() {
    return sample;
  }

  /**
   * Sets the sample.
   *
   * @param sample
   *          the new sample
   */
  public void setSample(Sample sample) {
    this.sample = sample;
  }

  /**
   * Gets the pcr batch.
   *
   * @return the pcr batch
   */
  @NotNull
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "PCRBatchID")
  public PcrBatch getPcrBatch() {
    return pcrBatch;
  }

  /**
   * Sets the pcr batch.
   *
   * @param pcrBatch
   *          the new pcr batch
   */
  public void setPcrBatch(PcrBatch pcrBatch) {
    this.pcrBatch = pcrBatch;
  }

  /**
   * Gets the mid.
   *
   * @return the mid
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "MID")
  public PcrPrimer getMid() {
    return mid;
  }

  /**
   * Sets the mid.
   *
   * @param mid
   *          the new mid
   */
  public void setMid(PcrPrimer mid) {
    this.mid = mid;
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
   *          the new primer reverse
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
   * Gets the round 1 reaction.
   *
   * @return the round 1 reaction
   */
  @OneToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "Round1ReactionID")
  public PcrReaction getRound1Reaction() {
    return round1Reaction;
  }

  /**
   * Sets the round 1 reaction.
   *
   * @param round1Reaction
   *          the new round 1 reaction
   */
  public void setRound1Reaction(PcrReaction round1Reaction) {
    this.round1Reaction = round1Reaction;
  }

  /**
   * Gets the unique id.
   *
   * @return the unique id
   */
  @Transient
  public Integer getUniqueId() {
    return getPcrReactionId();
  }

  /**
   * Sets the unique id.
   *
   * @param id
   *          the new unique id
   */
  public void setUniqueId(Integer id) {
    setPcrReactionId(id);
  }

  /**
   * Gets the display region.
   *
   * @return the display region
   */
  @Transient
  @JsonIgnore
  public Region getDisplayRegion() {
    if (this.region == null) {
      return pcrBatch.getRegion();
    } else {
      return this.region;
    }
  }

  /**
   * Gets the pcr reaction forward primer or the pcr batch forward primer
   * 
   * @return the forward primer
   */
  @Transient
  @JsonIgnore
  public PcrPrimer getDisplayPrimerForward() {
    if (this.primerForward == null) {
      return pcrBatch.getPrimerForward();
    } else {
      return this.primerForward;
    }
  }

  /**
   * Gets the pcr reaction reverse primer or the pcr batch reverse primer
   * 
   * @return the reverse primer
   */
  @Transient
  @JsonIgnore
  public PcrPrimer getDisplayPrimerReverse() {
    if (this.primerReverse == null) {
      return pcrBatch.getPrimerReverse();
    } else {
      return this.primerReverse;
    }
  }

  /**
   * Gets the tube number.
   *
   * @return the tube number
   */
  @NotNull
  @Column(name = "TubeNumber")
  public Integer getTubeNumber() {
    return this.tubeNumber;
  }

  /**
   * sets the tube number
   * 
   * @param tubeNumber
   */
  public void setTubeNumber(Integer tubeNumber) {
    this.tubeNumber = tubeNumber;
  }

  @Override
  @Transient
  @JsonIgnore
  public Group getAccessGroup() {
    return getGroup();
  }

}
