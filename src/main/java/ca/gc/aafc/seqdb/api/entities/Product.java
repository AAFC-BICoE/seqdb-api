/*
 * =====================================================================
 * Class:		Product.java
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Builder;

/**
 * The Class Product.
 */
@Entity
@Table(name = "Products", uniqueConstraints = {
@UniqueConstraint(columnNames = { "Name", "UPC" }) })
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Product {

  /** The product id. */
  private Integer productId;

  /** The name. */
  private String name;

  /** The upc. */
  private String upc;

  /** The type. */
  private String type;

  /** The description. */
  private String description;

  /** The last modified. */
  private Timestamp lastModified;

  /**
   * Instantiates a new product.
   */
  public Product() {
  }

  /**
   * Instantiates a new product.
   *
   * @param name
   *          the name
   * @param upc
   *          the upc
   * @param group
   *          the group
   */
  public Product(String name, String upc) {
    this.name = name;
    this.upc = upc;
  }

  /**
   * Instantiates a new product.
   *
   * @param name
   *          the name
   * @param upc
   *          the upc
   * @param type
   *          the type
   * @param description
   *          the description
   * @param group
   *          the group
   */
  @Builder
  public Product(String name, String upc, String type, String description) {
    this.name = name;
    this.upc = upc;
    this.type = type;
    this.description = description;
  }

  /**
   * Gets the product id.
   *
   * @return the product id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ProductID")
  public Integer getProductId() {
    return productId;
  }

  /**
   * Sets the product id.
   *
   * @param productId
   *          the new product id
   */
  public void setProductId(Integer productId) {
    this.productId = productId;
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
   * Gets the upc.
   *
   * @return the upc
   */
  @Size(max = 50)
  @Column(name = "UPC")
  public String getUpc() {
    return upc;
  }

  /**
   * Sets the upc.
   *
   * @param upc
   *          the new upc
   */
  public void setUpc(String upc) {
    this.upc = upc;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  @Size(max = 50)
  @Column(name = "Type")
  public String getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type
   *          the new type
   */
  public void setType(String type) {
    this.type = type;
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

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#getUniqueId()
   */
  @Transient
  public Integer getId() {
    return getProductId();
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setUniqueId(java.lang.Integer)
   */
  public void setId(Integer id) {
    setProductId(id);
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

}
