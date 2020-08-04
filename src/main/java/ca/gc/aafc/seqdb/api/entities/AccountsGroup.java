/*
 * =====================================================================
 * Class:		AccountsGroup.java
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class AccountsGroup.
 */
@Entity
@Table(name = "AccountsGroups")
public class AccountsGroup implements java.io.Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -6332997069790163515L;

  /** The account group id. */
  private Integer accountGroupId;

  /** The rights. */
  private String rights;

  /** The last modified. */
  private Timestamp lastModified;

  /** The account. */
  private Account account;

  /** The group. */
  private Group group;

  /** Whether the account is admin for this group. */
  private Boolean admin;

  /** Constants for predefined inputs */
  public static final String READ_ONLY = "READ_ONLY";

  public static final String ALL_ACCESS = "ALL_ACCESS";

  // Constructors

  /**
   * Instantiates a new accounts group.
   */
  public AccountsGroup() {
  }

  /**
   * Instantiates a new accounts group.
   *
   * @param rights
   *          the rights
   */
  public AccountsGroup(String rights) {
    this.rights = rights;
  }

  /**
   * Instantiates a new accounts group.
   *
   * @param rights
   *          the rights
   * @param account
   *          the account
   * @param group
   *          the group
   */
  public AccountsGroup(String rights, Account account, Group group) {
    this.rights = rights;
    this.account = account;
    this.group = group;
  }

  // Property accessors
  /**
   * Gets the account group id.
   *
   * @return the account group id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "AccountGroupID")
  public Integer getAccountGroupId() {
    return this.accountGroupId;
  }

  /**
   * Sets the account group id.
   *
   * @param accountGroupId
   *          the new account group id
   */
  public void setAccountGroupId(Integer accountGroupId) {
    this.accountGroupId = accountGroupId;
  }

  /**
   * Gets the rights.
   *
   * @return the rights
   */
  @NotNull
  @Size(min = 4, max = 4)
  @Column(name = "Rights")
  public String getRights() {
    return this.rights;
  }

  /**
   * Sets the rights.
   *
   * @param rights
   *          the new rights
   */
  public void setRights(String rights) {
    this.rights = rights;
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
   * Gets the account.
   *
   * @return the account
   */
  @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
  @JoinColumn(name = "AccountID")
  public Account getAccount() {
    return this.account;
  }

  /**
   * Sets the account.
   *
   * @param account
   *          the new account
   */
  public void setAccount(Account account) {
    this.account = account;
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
   * Gets the admin.
   *
   * @return the admin
   */
  @NotNull
  @Column(name = "Admin")
  public Boolean getAdmin() {
    return admin;
  }

  /**
   * Sets the admin.
   *
   * @param admin
   *          the new admin
   */
  public void setAdmin(Boolean admin) {
    this.admin = admin;
  }

  @Transient
  @JsonIgnore
  public boolean hasReadAccess() {
    return (rights == null || rights.length() != 4) ? false : rights.charAt(0) == '1';
  }

  @Transient
  @JsonIgnore
  public boolean hasWriteAccess() {
    return (rights == null || rights.length() != 4) ? false : rights.charAt(1) == '1';
  }

  @Transient
  @JsonIgnore
  public boolean hasDeleteAccess() {
    return (rights == null || rights.length() != 4) ? false : rights.charAt(2) == '1';
  }

  @Transient
  @JsonIgnore
  public boolean hasCreateAccess() {
    return (rights == null || rights.length() != 4) ? false : rights.charAt(3) == '1';
  }

}
