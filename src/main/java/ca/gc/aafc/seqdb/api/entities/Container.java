/*
 * =====================================================================
 * Class:		Container.java
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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.util.Pair;

import lombok.Builder;

@Entity
@Table(name = "Containers")
public class Container
    implements RestrictedByGroup {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 3006536341751315171L;

  /** The container id. */
  private Integer containerId;

  /** The container number. */
  private String containerNumber;

  /** The last modified. */
  private Timestamp lastModified;

  /** The container type. */
  private ContainerType containerType;

  /** The group. */
  private Group group;

  /** The locations. */
  private Set<Location> locations = new HashSet<Location>(0);

  // Constructors

  /**
   * Instantiates a new container.
   */
  public Container() {
  }

  /**
   * Instantiates a new container.
   *
   * @param containerNumber
   *          the container number
   * @param containerType
   *          the container type
   * @param group
   *          the group
   */
  public Container(String containerNumber, ContainerType containerType, Group group) {
    this.containerNumber = containerNumber;
    this.containerType = containerType;
    this.group = group;
  }
  
  /**
   * Instantiates a new container.
   *
   * @param containerNumber the container number
   * @param unitSection the unit section
   * @param storage the storage
   * @param locations the locations
   * @param containerType the container type
   * @param fillByRow fill by id.
   * @param group the group
   */
  @Builder
  public Container(String containerNumber, Set<Location> locations, 
		  ContainerType containerType, Group group) {
    this.containerNumber = containerNumber;
    this.locations = locations;
    this.containerType = containerType;
    this.group = group;
  }

  // Property accessors
  /**
   * Gets the container id.
   *
   * @return the container id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ContainerID")
  public Integer getContainerId() {
    return this.containerId;
  }

  /**
   * Sets the container id.
   *
   * @param containerId
   *          the new container id
   */
  public void setContainerId(Integer containerId) {
    this.containerId = containerId;
  }

  /**
   * Gets the container number.
   *
   * @return the container number
   */
  @NotNull
  @Size(max = 50)
  @Column(name = "ContainerNumber")
  public String getContainerNumber() {
    return this.containerNumber;
  }

  /**
   * Sets the container number.
   *
   * @param containerNumber
   *          the new container number
   */
  public void setContainerNumber(String containerNumber) {
    this.containerNumber = containerNumber;
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
   * Gets the locations.
   *
   * @return the locations
   */
  @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "container")
  @OrderBy("locationId")
  public Set<Location> getLocations() {
    return this.locations;
  }

  /**
   * Sets the locations.
   *
   * @param locations
   *          the new locations
   */
  public void setLocations(Set<Location> locations) {
    this.locations = locations;
  }

  /**
   * Gets the container type.
   *
   * @return the container type
   */
  @NotNull
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "ContainerTypeID")
  public ContainerType getContainerType() {
    return this.containerType;
  }

  /**
   * Sets the container type.
   *
   * @param containerType
   *          the new container type
   */
  public void setContainerType(ContainerType containerType) {
    this.containerType = containerType;
  }

  /**
   * Gets the group.
   *
   * @return the group
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "GroupID")
  public Group getGroup() {
    return group;
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

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getUniqueId()
   */
  @Transient
  public Integer getId() {
    return this.containerId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setContainerId(id);
  }

  @Override
  @JsonIgnore
  @Transient
  public Group getAccessGroup() {
    return getGroup();
  }

  /**
   * Computes the index of a given row and column location via left to right. ie. for a 3x3 square
   * the indices would be
   * 
   * 1 2 3 4 5 6 7 8 9
   * 
   * @param rowNumber
   *          of the index you want to retrieve
   * @param columnNumber
   *          of the index you want to retrieve
   * @param maxRow
   *          the number of rows in the container
   * @param maxColumn
   *          the number of columns in the container
   * @return the index of the location
   */

  public static int computeCellAddressByRow(int rowNumber, int columnNumber, int maxRow,
      int maxColumn) {
    if (rowNumber <= 0 || columnNumber <= 0) {
      throw new IllegalArgumentException("Column and/or row number shall be greater than 0");
    }
    if (columnNumber > maxColumn || rowNumber > maxRow) {
      throw new IllegalArgumentException(
          "Column and/or row number can't be greater than their respective max");
    }
    return (((rowNumber - 1) * maxColumn) + columnNumber);
  }

  /**
   * Computes the index of a given row and column location via top to bottom. ie. for a 3x3 square
   * the indices would be
   * 
   * 1 4 7 2 5 8 3 6 9
   * 
   * @param rowNumber
   *          of the index you want to retrieve
   * @param columnNumber
   *          of the index you want to retrieve
   * @param maxRow
   *          the number of rows in the container
   * @param maxColumn
   *          the number of columns in the container
   * @return the index of the location
   */

  public static int computeCellAddressByColumn(int rowNumber, int columnNumber, int maxRow,
      int maxColumn) {
    if (rowNumber <= 0 || columnNumber <= 0) {
      throw new IllegalArgumentException("Column and/or row number shall be greater than 0");
    }
    if (columnNumber > maxColumn || rowNumber > maxRow) {
      throw new IllegalArgumentException(
          "Column and/or row number can't be greater than their respective max");
    }
    return (((columnNumber - 1) * maxRow) + rowNumber);
  }

  /**
   * The reciprocal of the computeCellAdressByRow method, converts an index to it's column and row.
   * 
   * @param index
   * @param maxColumn
   *          the container's maximum number of Columns
   * @return
   */
  public static Pair<Integer, Integer> computeLocationFromAddressByRow(int index, int maxColumn) {
    if (index <= 0) {
      throw new IllegalArgumentException("Column and/or row number shall be greater than 0");
    }
    if (maxColumn <= 0) {
      throw new IllegalArgumentException("The maximum number of Column cannot be 0 or less");
    }
    return Pair.of((int) ((index - 1) / maxColumn) + 1, ((index - 1) % maxColumn) + 1);
  }

  /**
   * The reciprocal of the computeCellAdressByColumn method, converts an index to it's column and
   * row.
   * 
   * @param index
   * @param maxRow
   *          the container's maximum number of Row
   * @return
   */
  public static Pair<Integer, Integer> computeLocationFromAddressByColumn(int index, int maxRow) {
    if (index <= 0) {
      throw new IllegalArgumentException("Column and/or row number shall be greater than 0");
    }
    if (maxRow <= 0) {
      throw new IllegalArgumentException("The maximum number of rows cannot be 0 or less");
    }
    return Pair.of(((index - 1) % maxRow) + 1, (int) ((index - 1) / maxRow) + 1);

  }

}
