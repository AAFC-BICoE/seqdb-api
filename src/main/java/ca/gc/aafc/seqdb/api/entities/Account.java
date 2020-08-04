/*
 * =====================================================================
 * Class:		Account.java
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

import java.sql.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name = "Accounts")
public class Account implements java.io.Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -5655824300348079540L;

  /** The account id. */
  private Integer accountId;

  /** The account name. */
  private String accountName;

  /** The account pw. */
  private String accountPw;

  /** The account status. */
  private String accountStatus = Status.ACTIVE.toString();

  /** The account expires. */
  private Date accountExpires;

  /** The account type. */
  private String accountType;

  /** The last login. */
  private Timestamp lastLogin;

  /** The last login from. */
  private String lastLoginFrom;

  /** The ldap dn. */
  private String ldapDn;

  /** The api key. */
  private String apiKey;

  /** The last modified. */
  private Timestamp lastModified;

  /** The accounts groups. */
  private Set<AccountsGroup> accountsGroups = new HashSet<AccountsGroup>(0);

  /**
   * enum for account types, does not override existing accountType instead offers a way to
   * initiating account types with standardized string inputs.
   */
  public static enum Type {
    ADMIN("Admin"), USER("User"), GUEST("Guest");

    private String value;

    private Type(String s) {
      value = s;
    }

    @Override
    public String toString() {
      return value;
    }

  }

  /**
   * enum for account types, does not override existing accountType instead offers a way to
   * initiating account types with standardized string inputs.
   */
  public enum Status {
    ACTIVE("Active"), EXPIRED("Expired"), DISABLED("Disabled");

    private String value;

    private Status(String s) {
      value = s;
    }

    @Override
    public String toString() {
      return value;
    }

  }

  // Constructors
  /**
   * Instantiates a new account.
   */
  public Account() {
  }

  /**
   * Instantiates a new account.
   *
   * @param accountName
   *          the account name
   * @param accountType
   *          the account type
   */
  public Account(String accountName, String accountType) {
    this.accountName = accountName;
    this.accountType = accountType;
  }

  /**
   * Instantiates a new account.
   *
   * @param accountName
   *          the account name
   * @param accountPw
   *          the account pw
   * @param accountStatus
   *          the account status
   * @param accountExpires
   *          the account expires
   * @param accountType
   *          the account type
   * @param lastLogin
   *          the last login
   * @param lastLoginFrom
   *          the last login from
   * @param accountsGroups
   *          the accounts groups
   * @param accountPreferences
   *          the account preferences
   * @param accountProfile
   *          the account profile
   * @param people
   *          the people
   */
  public Account(String accountName, String accountPw, String accountStatus, Date accountExpires,
      String accountType, Timestamp lastLogin, String lastLoginFrom,
      Set<AccountsGroup> accountsGroups) {
    this.accountName = accountName;
    this.accountPw = accountPw;
    this.accountStatus = accountStatus;
    this.accountExpires = accountExpires == null ? null : Date.valueOf(accountExpires.toLocalDate());
    this.accountType = accountType;
    this.lastLogin = lastLogin == null ? null : new Timestamp(lastLogin.getTime());
    this.lastLoginFrom = lastLoginFrom;
    this.accountsGroups = accountsGroups;
  }

  // Property accessors
  /**
   * Gets the account id.
   *
   * @return the account id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "AccountID")
  public Integer getAccountId() {
    return this.accountId;
  }

  /**
   * Sets the account id.
   *
   * @param accountId
   *          the new account id
   */
  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  /**
   * Gets the account name.
   *
   * @return the account name
   */
  @NotNull
  @Size(max = 20)
  @Column(name = "AccountName", unique = true)
  public String getAccountName() {
    return this.accountName;
  }

  /**
   * Sets the account name.
   *
   * @param accountName
   *          the new account name
   */
  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  /**
   * Gets the account pw.
   *
   * @return the account pw
   */
  @Size(max = 100)
  @Column(name = "AccountPw")
  public String getAccountPw() {
    return this.accountPw;
  }

  /**
   * Sets the account pw.
   *
   * @param accountPw
   *          the new account pw
   */
  public void setAccountPw(String accountPw) {
    this.accountPw = accountPw;
  }

  /**
   * Gets the account status.
   *
   * @return the account status
   */
  @Size(max = 20)
  @Pattern(regexp = "(Active|Expired|Disabled)")
  @Column(name = "AccountStatus")
  public String getAccountStatus() {
    return this.accountStatus;

  }

  /**
   * Sets the account status.
   *
   * @param accountStatus
   *          the new account status
   */
  public void setAccountStatus(String accountStatus) {
    this.accountStatus = accountStatus;
  }

  /**
   * Gets the account expires.
   *
   * @return the account expires
   */
  @Column(name = "AccountExpires")
  public Date getAccountExpires() {
    return this.accountExpires == null ? null : Date.valueOf(this.accountExpires.toLocalDate());
  }

  /**
   * Sets the account expires.
   *
   * @param accountExpires
   *          the new account expires
   */
  public void setAccountExpires(Date accountExpires) {
    this.accountExpires = accountExpires == null ? null : Date.valueOf(accountExpires.toLocalDate());
  }

  /**
   * Gets the account type.
   *
   * @return the account type
   */
  @NotNull
  @Size(max = 20)
  @Pattern(regexp = "(User|Admin|Guest)")
  @Column(name = "AccountType")
  public String getAccountType() {
    return this.accountType;

  }

  /**
   * Sets the account type.
   *
   * @param accountType
   *          the new account type
   */
  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  /**
   * Gets the last login.
   *
   * @return the last login
   */
  @Column(name = "LastLogin")
  public Timestamp getLastLogin() {
    return this.lastLogin == null ? null : new Timestamp(this.lastLogin.getTime());
  }

  /**
   * Sets the last login.
   *
   * @param lastLogin
   *          the new last login
   */
  public void setLastLogin(Timestamp lastLogin) {
    this.lastLogin = lastLogin == null ? null : new Timestamp(lastLogin.getTime());
  }

  /**
   * Gets the last login from.
   *
   * @return the last login from
   */
  @Size(max = 50)
  @Column(name = "LastLoginFrom")
  public String getLastLoginFrom() {
    return this.lastLoginFrom;
  }

  /**
   * Sets the last login from.
   *
   * @param lastLoginFrom
   *          the new last login from
   */
  public void setLastLoginFrom(String lastLoginFrom) {
    this.lastLoginFrom = lastLoginFrom;
  }

  /**
   * Gets the ldap dn.
   *
   * @return the ldap dn
   */
  @Size(max = 200)
  @Column(name = "LdapDn", unique = true)
  public String getLdapDn() {
    return this.ldapDn;
  }

  /**
   * Sets the ldap dn.
   *
   * @param ldapDn
   *          the new ldap dn
   */
  public void setLdapDn(String ldapDn) {
    this.ldapDn = ldapDn;
  }

  /**
   * Gets the api key.
   *
   * @return the api key
   */
  @Size(max = 100)
  @Column(name = "ApiKey", unique = true)
  public String getApiKey() {
    return this.apiKey;
  }

  /**
   * Sets the api key.
   *
   * @param apiKey
   *          the new api key
   */
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  /**
   * Gets the last modified.
   *
   * @return the last modified
   */
  @Version
  @Column(name = "LastModified")
  public Timestamp getLastModified() {
    return this.lastModified == null ? null : new Timestamp(this.lastModified.getTime());
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
    this.lastModified = lastModified == null ? null : new Timestamp(lastModified.getTime());
  }

  /**
   * Gets the accounts groups.
   *
   * @return the accounts groups
   */
  @OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy = "account")
  @OrderBy("accountGroupId")
  @NotAudited
  public Set<AccountsGroup> getAccountsGroups() {
    return this.accountsGroups;
  }

  /**
   * Sets the accounts groups.
   *
   * @param accountsGroups
   *          the new accounts groups
   */
  public void setAccountsGroups(Set<AccountsGroup> accountsGroups) {
    this.accountsGroups = accountsGroups;
  }

}
