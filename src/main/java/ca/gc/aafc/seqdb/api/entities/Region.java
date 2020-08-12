/*
 * =====================================================================
 * Class:		Region.java
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.Builder;

/**
 * The Class Region.
 */
@Entity

@Table(name = "Regions", uniqueConstraints = {
    @UniqueConstraint(columnNames = "Name") })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class Region implements Serializable {

  private static final long serialVersionUID = -1006757795446154087L;

  private Integer regionId;

  /** The symbol. */
  private String symbol;

  private String name;

  private String description;

  /** The aliases. */
  private String aliases;

  /** The applicable organisms. */
  private String applicableOrganisms;

  /**
   * Instantiates a new region.
   */
  public Region() {
    super();
  }

  @Builder
  public Region(String name, String description,
      String symbol, String aliases, String applicableOrganisms) {
    this.name = name;
    this.description = description;
    this.symbol = symbol;
    this.aliases = aliases;
    this.applicableOrganisms = applicableOrganisms;
  }

  // Property accessors

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "RegionID")
  public Integer getRegionId() {
    return this.regionId;
  }

  public void setRegionId(Integer regionId) {
    this.regionId = regionId;
  }


  /**
   * Gets the symbol.
   *
   * @return the symbol
   */
  @NotNull
  @Size(max = 50)
  @Column(name = "Symbol")
  public String getSymbol() {
    return symbol;
  }

  /**
   * Sets the symbol.
   *
   * @param symbol
   *          the new symbol
   */
  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "Description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the aliases.
   *
   * @return the aliases
   */
  @Size(max = 255)
  @Column(name = "Aliases")
  public String getAliases() {
    return aliases;
  }

  /**
   * Sets the aliases.
   *
   * @param aliases
   *          the new aliases
   */
  public void setAliases(String aliases) {
    this.aliases = aliases;
  }

  /**
   * Gets the applicable organisms.
   *
   * @return the applicable organisms
   */
  @Size(max = 255)
  @Column(name = "ApplicableOrganisims")
  public String getApplicableOrganisms() {
    return applicableOrganisms;
  }

  /**
   * Sets the applicable organisms.
   *
   * @param applicableOrganisms
   *          the new applicable organisms
   */
  public void setApplicableOrganisms(String applicableOrganisms) {
    this.applicableOrganisms = applicableOrganisms;
  }

}
