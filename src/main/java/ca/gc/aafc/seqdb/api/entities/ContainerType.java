/*
 * =====================================================================
 * Class:		ContainerType.java
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

// Generated 10-May-2007 10:53:34 by Hibernate Tools 3.2.0.beta8

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;

/**
 * The Class ContainerType.
 */
@Entity

@Table(name = "ContainerTypes", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "Name" }) })
public class ContainerType {

  /** The container type id. */
  private Integer containerTypeId;

  /** The name. */
  private String name;

  /** 
   * Physical description of the container type
   * e.g. Tube rack, cryobox
   */
  private String baseType;

  /** The number of wells. */
  private Integer numberOfWells;

  /** The number of columns. */
  private Integer numberOfColumns;

  /** The number of rows. */
  private Integer numberOfRows;

  /** The last modified. */
  private Timestamp lastModified;

  /** The physical height of the container type */
  private Integer heightInMM;

  /** The physical width of the container type */
  private Integer widthInMM;

  private static final String DEFAULT_BASE_TYPE = "Default container type";

  // Constructors

  /**
   * Instantiates a new container type.
   */
  public ContainerType() {
  }

  /**
   * Instantiates a new container type.
   *
   * @param name
   *          the name
   * @param baseType
   *          the base type
   * @param numberOfWells
   *          the number of wells
   * @param numberOfColumns
   *          the number of columns
   * @param numberOfRows
   *          the number of rows
   * @param heightInMM
   *          the physical height in milimeters
   * @param widthInMM
   *          the physical width in milimeters
   * @param group
   *          the group
   */
  @Builder
  public ContainerType(String name, String baseType, Integer numberOfWells, Integer numberOfColumns,
      Integer numberOfRows, Integer heightInMM, Integer widthInMM) {
    this.name = name;
    this.baseType = baseType;
    this.numberOfWells = numberOfWells;
    this.numberOfColumns = numberOfColumns;
    this.numberOfRows = numberOfRows;
    this.heightInMM = heightInMM;
    this.widthInMM = widthInMM;
  }

  // Property accessors
  /**
   * Gets the container type id.
   *
   * @return the container type id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ContainerTypeID")
  public Integer getContainerTypeId() {
    return this.containerTypeId;
  }

  /**
   * Sets the container type id.
   *
   * @param containerTypeId
   *          the new container type id
   */
  public void setContainerTypeId(Integer containerTypeId) {
    this.containerTypeId = containerTypeId;
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
   * Gets the base type.
   *
   * @return the base type
   */
  @NotNull
  @Size(max = 50)
  @Column(name = "BaseType")
  public String getBaseType() {
    return baseType;
  }

  /**
   * Sets the base type.
   *
   * @param baseType
   *          the new base type
   */
  public void setBaseType(String baseType) {
    this.baseType = baseType;
  }

  /**
   * Gets the number of wells.
   *
   * @return the number of wells
   */
  @NotNull
  @Column(name = "NumberOfWells")
  public Integer getNumberOfWells() {
    return this.numberOfWells;
  }

  /**
   * Sets the number of wells.
   *
   * @param numberOfWells
   *          the new number of wells
   */
  public void setNumberOfWells(Integer numberOfWells) {
    this.numberOfWells = numberOfWells;
  }

  /**
   * Gets the number of columns.
   *
   * @return the number of columns
   */
  @NotNull
  @Column(name = "NumberOfColumns")
  public Integer getNumberOfColumns() {
    return this.numberOfColumns;
  }

  /**
   * Sets the number of columns.
   *
   * @param numberOfColumns
   *          the new number of columns
   */
  public void setNumberOfColumns(Integer numberOfColumns) {
    this.numberOfColumns = numberOfColumns;
  }

  /**
   * Gets the number of rows.
   *
   * @return the number of rows
   */
  @NotNull
  @Column(name = "NumberOfRows")
  public Integer getNumberOfRows() {
    return this.numberOfRows;
  }

  /**
   * Sets the number of rows.
   *
   * @param numberOfRows
   *          the new number of rows
   */
  public void setNumberOfRows(Integer numberOfRows) {
    this.numberOfRows = numberOfRows;
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
    return containerTypeId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setContainerTypeId(id);
  }

  /**
   * Gets the physical height of the container type
   * 
   * @return the height of the container in milimeters
   */
  @Min(1)
  @Max(1000)
  @Column(name = "HeightInMM")
  public Integer getHeightInMM() {
    return heightInMM;
  }

  /**
   * Sets the physical height of this container type
   * 
   * @param heightInMM
   *          the height of the container type in milimeters
   */
  public void setHeightInMM(Integer heightInMM) {
    this.heightInMM = heightInMM;
  }

  /**
   * Gets the physical width of this container type
   * 
   * @return the width of this container type in milimeters
   */
  @Min(1)
  @Max(1000)
  @Column(name = "WidthInMM")
  public Integer getWidthInMM() {
    return widthInMM;
  }

  /**
   * Sets the physical width of this container type
   * 
   * @param widthInMM
   *          the width of this container type in milimeters
   */
  public void setWidthInMM(Integer widthInMM) {
    this.widthInMM = widthInMM;
  }

  /**
   * Simple validator that checks the argument integers against the containerType's rows and column
   * to see if they mismatch
   * 
   * @param columnnumber
   * @param rownumber
   * @return true if the number of rows and columns in the container is more than the columnnumber
   *         and rownumber
   */
  public boolean isValidLocation(int columnnumber, int rownumber) {

    return (getNumberOfColumns() >= columnnumber && getNumberOfRows() >= rownumber);

  }

  @Transient
  public boolean isDefaultContainerType() {
    return Objects.equals(baseType, DEFAULT_BASE_TYPE);
  }
}
