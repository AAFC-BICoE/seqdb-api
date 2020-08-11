/*
 * =====================================================================
 * Class:		Protocol.java
 * Package: 	ca.gc.aafc.seqdb.api.entities
 * 
 * Author:  Nazir El-Kayssi
 * Date:    2016-06-28
 * Contact: nazir.el-kayssi@agr.gc.ca
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Builder;

/**
 * The Class Protocol.
 * 
 * @author elkayssin
 */
@Entity

@Table(name = "Protocols", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "Name", "Type" }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class Protocol {

  /**
   * The Enum ProtocolType.
   */
  public enum ProtocolType {

    /** The collection event. */
    COLLECTION_EVENT("Collection Event"),

    /** The specimen preparation. */
    SPECIMEN_PREPARATION("Specimen Preparation"),

    /** The dna extraction. */
    DNA_EXTRACTION("DNA Extraction"),

    /** The pcr reaction. */
    PCR_REACTION("PCR Reaction"),

    /** The seq reaction. */
    SEQ_REACTION("Sequencing Reaction");

    /** The value. */
    private final String value;

    /**
     * Instantiates a new protocol type.
     *
     * @param value
     *          the value
     */
    ProtocolType(String value) {
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

  /** The protocol id. */
  private Integer protocolId;

  /** The type. */
  private ProtocolType type;

  /** The name. */
  private String name;

  /** The version. */
  private String version;

  /** The description. */
  private String description;

  /** The steps. */
  private String steps;

  /** The notes. */
  private String notes;

  /** The reference. */
  private String reference;

  /** The equipment. */
  private String equipment;

  /** The forward primer concentration. */
  private String forwardPrimerConcentration;

  /** The reverse primer concentration. */
  private String reversePrimerConcentration;

  /** The reaction mix volume. */
  private String reactionMixVolume;

  /** The reaction mix volume per tube. */
  private String reactionMixVolumePerTube;

  /** The kit. */
  private Product kit;

  /** The last modified. */
  private Timestamp lastModified;

  /**
   * Instantiates a new protocol.
   */
  public Protocol() {
  }

  /**
   * Instantiates a new protocol.
   *
   * @param type
   *          the type
   * @param name
   *          the name
   * @param version
   *          the version
   * @param description
   *          the description
   * @param steps
   *          the steps
   * @param notes
   *          the notes
   * @param reference
   *          the reference
   * @param equipment
   *          the equipment
   * @param group
   *          the group
   * @param kit
   *          the kit
   */
  public Protocol(ProtocolType type, String name, String version, String description, String steps,
      String notes, String reference, String equipment, Product kit) {
    this.type = type;
    this.name = name;
    this.version = version;
    this.description = description;
    this.steps = steps;
    this.notes = notes;
    this.reference = reference;
    this.equipment = equipment;
    this.kit = kit;
  }

  /**
   * Instantiates a new protocol.
   *
   * @param protocolId
   *          the protocol id
   * @param type
   *          the type
   * @param name
   *          the name
   * @param version
   *          the version
   * @param description
   *          the description
   * @param steps
   *          the steps
   * @param notes
   *          the notes
   * @param reference
   *          the reference
   * @param equipment
   *          the equipment
   * @param forwardPrimerConcentration
   *          the forward primer concentration
   * @param reversePrimerConcentration
   *          the reverse primer concentration
   * @param reactionMixVolume
   *          the reaction mix volume
   * @param reactionMixVolumePerTube
   *          the reaction mix volume per tube
   * @param group
   *          the group
   * @param kit
   *          the kit
   */
  @Builder
  public Protocol(Integer protocolId, ProtocolType type, String name, String version,
      String description, String steps, String notes, String reference, String equipment,
      String forwardPrimerConcentration, String reversePrimerConcentration,
      String reactionMixVolume, String reactionMixVolumePerTube, Product kit) {
    super();
    this.protocolId = protocolId;
    this.type = type;
    this.name = name;
    this.version = version;
    this.description = description;
    this.steps = steps;
    this.notes = notes;
    this.reference = reference;
    this.equipment = equipment;
    this.forwardPrimerConcentration = forwardPrimerConcentration;
    this.reversePrimerConcentration = reversePrimerConcentration;
    this.reactionMixVolume = reactionMixVolume;
    this.reactionMixVolumePerTube = reactionMixVolumePerTube;
    this.kit = kit;
  }

  /**
   * Gets the protocol id.
   *
   * @return the protocol id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ProtocolID")
  public Integer getProtocolId() {
    return protocolId;
  }

  /**
   * Sets the protocol id.
   *
   * @param protocolId
   *          the new protocol id
   */
  public void setProtocolId(Integer protocolId) {
    this.protocolId = protocolId;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  @NotNull
  @Column(name = "Type")
  @Enumerated(EnumType.STRING)
  public ProtocolType getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type
   *          the new type
   */
  public void setType(ProtocolType type) {
    this.type = type;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @NotBlank
  @Size(max = 50)
  @Column(name = "Name")
  public String getName() {
    return name;
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
  @Size(max = 5)
  @Column(name = "Version")
  public String getVersion() {
    return version;
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
   * Gets the description.
   *
   * @return the description
   */
  @Size(max = 200)
  @Column(name = "Description")
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param description
   *          the new description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the steps.
   *
   * @return the steps
   */
  @Column(name = "Steps")
  public String getSteps() {
    return steps;
  }

  /**
   * Sets the steps.
   *
   * @param steps
   *          the new steps
   */
  public void setSteps(String steps) {
    this.steps = steps;
  }

  /**
   * Gets the notes.
   *
   * @return the notes
   */
  @Size(max = 200)
  @Column(name = "Notes")
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
   * Gets the reference.
   *
   * @return the reference
   */
  @Size(max = 100)
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
   * Gets the equipment.
   *
   * @return the equipment
   */
  @Size(max = 50)
  @Column(name = "Equipment")
  public String getEquipment() {
    return equipment;
  }

  /**
   * Sets the equipment.
   *
   * @param equipment
   *          the new equipment
   */
  public void setEquipment(String equipment) {
    this.equipment = equipment;
  }

  /**
   * Gets the forward primer concentration.
   *
   * @return the forward primer concentration
   */
  @Size(max = 50)
  @Column(name = "ForwardPrimerConcentration")
  public String getForwardPrimerConcentration() {
    return forwardPrimerConcentration;
  }

  /**
   * Sets the forward primer concentration.
   *
   * @param forwardPrimerConcentration
   *          the new forward primer concentration
   */
  public void setForwardPrimerConcentration(String forwardPrimerConcentration) {
    this.forwardPrimerConcentration = forwardPrimerConcentration;
  }

  /**
   * Gets the reverse primer concentration.
   *
   * @return the reverse primer concentration
   */
  @Size(max = 50)
  @Column(name = "ReversePrimerConcentration")
  public String getReversePrimerConcentration() {
    return reversePrimerConcentration;
  }

  /**
   * Sets the reverse primer concentration.
   *
   * @param reversePrimerConcentration
   *          the new reverse primer concentration
   */
  public void setReversePrimerConcentration(String reversePrimerConcentration) {
    this.reversePrimerConcentration = reversePrimerConcentration;
  }

  /**
   * Gets the reaction mix volume.
   *
   * @return the reaction mix volume
   */
  @Size(max = 50)
  @Column(name = "ReactionMixVolume")
  public String getReactionMixVolume() {
    return reactionMixVolume;
  }

  /**
   * Sets the reaction mix volume.
   *
   * @param reactionMixVolume
   *          the new reaction mix volume
   */
  public void setReactionMixVolume(String reactionMixVolume) {
    this.reactionMixVolume = reactionMixVolume;
  }

  /**
   * Gets the reaction mix volume per tube.
   *
   * @return the reaction mix volume per tube
   */
  @Size(max = 50)
  @Column(name = "ReactionMixVolumePerTube")
  public String getReactionMixVolumePerTube() {
    return reactionMixVolumePerTube;
  }

  /**
   * Sets the reaction mix volume per tube.
   *
   * @param reactionMixVolumePerTube
   *          the new reaction mix volume per tube
   */
  public void setReactionMixVolumePerTube(String reactionMixVolumePerTube) {
    this.reactionMixVolumePerTube = reactionMixVolumePerTube;
  }

  /**
   * Gets the kit.
   *
   * @return the kit
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "ProductID")
  public Product getKit() {
    return kit;
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
   * Gets the last modified.
   *
   * @return the last modified
   */
  @Version
  @Column(name = "LastModified")
  public Timestamp getLastModified() {
    return lastModified;
  }

  /**
   * Sets the last modified.
   * 
   * Note that with @Version defined for the lastModified field, every time this object is created
   * or modified, lastModified is set automatically, so no explicit set is needed for this field.
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
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getId()
   */
  @Transient
  public Integer getId() {
    return getProtocolId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setProtocolId(id);
  }

}
