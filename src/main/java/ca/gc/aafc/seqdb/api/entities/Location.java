/*
 * =====================================================================
 * Class:		Location.java
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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;




import lombok.Builder;

/**
 * The Class Location.
 */
@Entity
@Table(name = "Locations", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "ContainerID", "WellColumn", "WellRow" }) })
public class Location
    implements RestrictedByGroup {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 512382894194207007L;

  /** The location id. */
  private Integer locationId;

  /** The well column. */
  private Integer wellColumn;

  /** The well row. */
  private String wellRow;

  /** The date moved. */
  private Timestamp dateMoved;

  /** The last modified. */
  private Timestamp lastModified;

  /** The container. */
  private Container container;

  // If a relation to a new entity is added, the PostgreSQL check function
  // chk_contains_exactly_one_notnull should also be updated (see #18194)
  private Sample sample;
  private PcrPrimer pcrPrimer;

  public Location() {
  }

  /**
   * Instantiates a new location.
   *
   * @param wellColumn
   *          the well column
   * @param wellRow
   *          the well row
   * @param container
   *          the container
   */
  public Location(Integer wellColumn, String wellRow, Container container) {
    this.wellColumn = wellColumn;
    this.wellRow = wellRow;
    this.container = container;
  }

  /**
   * Instantiates a new location.
   *
   * @param wellColumn
   *          the well column
   * @param wellRow
   *          the well row
   * @param wellCoordinates
   *          the well coordinates
   * @param dateMoved
   *          the date moved
   * @param container
   *          the container
   * @param specimenReplicate
   *          the specimen replicate
   * @param sample
   *          the sample
   * @param pcrPrimer
   *          the pcr primer
   * @param mixedSpecimen
   *          the mixed specimen
   */
  @Builder
  public Location(Integer wellColumn, String wellRow, String wellCoordinates, Timestamp dateMoved,
      Container container, Sample sample, PcrPrimer pcrPrimer) {
    this.wellColumn = wellColumn;
    this.wellRow = wellRow;
    this.dateMoved = dateMoved;
    this.container = container;
    this.sample = sample;
    this.pcrPrimer = pcrPrimer;
  }

  // Property accessors

  /**
   * Gets the location id.
   *
   * @return the location id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "LocationID")
  public Integer getLocationId() {
    return this.locationId;
  }

  /**
   * Sets the location id.
   *
   * @param locationId
   *          the new location id
   */
  public void setLocationId(Integer locationId) {
    this.locationId = locationId;
  }

  /**
   * Gets the well column.
   *
   * @return the well column
   */
  @Min(value = 1)
  @Column(name = "WellColumn")
  public Integer getWellColumn() {
    return this.wellColumn;
  }

  /**
   * Sets the well column.
   *
   * @param wellColumn
   *          the new well column
   */
  public void setWellColumn(Integer wellColumn) {
    this.wellColumn = wellColumn;
  }

  /**
   * Gets the well row.
   *
   * @return the well row
   */
  @Size(max = 2)
  @Pattern(regexp = "[a-zA-Z]+")
  @Column(name = "WellRow")
  public String getWellRow() {
    return this.wellRow;
  }

  /**
   * Sets the well row.
   *
   * @param wellRow
   *          the new well row
   */
  public void setWellRow(String wellRow) {
    this.wellRow = wellRow;
  }

  /**
	 * Gets the well coordinates.
	 *
	 * @return the well coordinates
	 */
  @Transient
	@JsonIgnore
	public String getWellCoordinates() {
		return new StringBuilder()
			.append(wellColumn != null ? wellColumn : "")
			.append(wellRow != null ? wellRow : "")
			.toString();
	}

  /**
   * Gets the date moved.
   *
   * @return the date moved
   */
  @Column(name = "DateMoved")
  public Timestamp getDateMoved() {
    return this.dateMoved;
  }

  /**
   * Sets the date moved.
   *
   * @param dateMoved
   *          the new date moved
   */
  public void setDateMoved(Timestamp dateMoved) {
    this.dateMoved = dateMoved;
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
   * Gets the container.
   *
   * @return the container
   */
  @NotNull
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "ContainerID")
  public Container getContainer() {
    return this.container;
  }

  /**
   * Sets the container.
   *
   * @param container
   *          the new container
   */
  public void setContainer(Container container) {
    this.container = container;
  }

  /**
   * Gets the sample.
   *
   * @return the sample
   */
  @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REFRESH }, fetch = FetchType.LAZY)
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
   * Gets the pcr primer.
   *
   * @return the pcr primer
   */
  @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REFRESH }, fetch = FetchType.LAZY)
  @JoinColumn(name = "PcrPrimerID")
  public PcrPrimer getPcrPrimer() {
    return pcrPrimer;
  }

  /**
   * Sets the pcr primer.
   *
   * @param pcrPrimer
   *          the new pcr primer
   */
  public void setPcrPrimer(PcrPrimer pcrPrimer) {
    this.pcrPrimer = pcrPrimer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getUniqueId()
   */
  @Transient
  public Integer getId() {
    return this.locationId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setLocationId(id);
  }

  @Override
  @Transient
  @JsonIgnore
  public Group getAccessGroup() {
    return getContainer().getAccessGroup();
  }

}
