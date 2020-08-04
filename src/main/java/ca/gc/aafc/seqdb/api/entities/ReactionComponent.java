package ca.gc.aafc.seqdb.api.entities;

import java.io.Serializable;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;



import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The Class ReactionComponent.
 */
@Entity
@Table(name = "ReactionComponents")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class ReactionComponent
    implements  RestrictedByGroup {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = -3251141369889763346L;

  /** The reaction component id. */
  private Integer reactionComponentId;

  /** The Name. */
  private String name;

  /** The concentration. */
  private String concentration;

  /** The quantity. */
  private Float quantity;

  /** The protocol. */
  private Protocol protocol;

  /** The last modified. */
  private Timestamp lastModified;

  public ReactionComponent() {

  }

  @Builder
  public ReactionComponent(String name, String concentration, Float quantity, Protocol protocol,
      Timestamp lastModified) {
    this.name = name;
    this.concentration = concentration;
    this.quantity = quantity;
    this.protocol = protocol;
    this.lastModified = lastModified;
  }

  /**
   * Gets the reaction component id.
   *
   * @return the reaction component id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ReactionComponentID")
  public Integer getReactionComponentId() {
    return reactionComponentId;
  }

  /**
   * Sets the reaction component id.
   *
   * @param reactionComponentId
   *          the new reaction component id
   */
  public void setReactionComponentId(Integer reactionComponentId) {
    this.reactionComponentId = reactionComponentId;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
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
   * Gets the concentration.
   *
   * @return the concentration
   */
  @Column(name = "Concentration")
  public String getConcentration() {
    return concentration;
  }

  /**
   * Sets the concentration.
   *
   * @param concentration
   *          the new concentration
   */
  public void setConcentration(String concentration) {
    this.concentration = concentration;
  }

  /**
   * Gets the quantity.
   *
   * @return the quantity
   */
  @Column(name = "Quantity")
  public Float getQuantity() {
    return quantity;
  }

  /**
   * Sets the quantity.
   *
   * @param quantity
   *          the new quantity
   */
  public void setQuantity(Float quantity) {
    this.quantity = quantity;
  }

  /**
   * Gets the protocol.
   *
   * @return the protocol
   */
  @NotNull
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
    return this.reactionComponentId;
  }

  /*
   * (non-Javadoc)
   * 
   * @see ca.gc.aafc.seqdb.interfaces.UniqueObj#setId(java.lang.Integer)
   */
  public void setId(Integer id) {
    this.reactionComponentId = id;
  }

  @Override
  @Transient
  @JsonIgnore
  public Group getAccessGroup() {
    return getProtocol().getAccessGroup();
  }

}
