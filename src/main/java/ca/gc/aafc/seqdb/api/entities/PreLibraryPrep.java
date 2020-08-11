package ca.gc.aafc.seqdb.api.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Builder;

/**
 * Entity class for PreLibraryPrep.
 * 
 * The Pre-Library Prep entity is used in a workflow step to record shearing or size selection
 * performed on a Sample.
 *
 */
@Entity

@Table(name = "PreLibraryPreps")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class PreLibraryPrep implements Serializable {

  /**
   * Generated serial version.
   */
  private static final long serialVersionUID = -8583083748458701615L;

  public enum PreLibraryPrepType {
    SHEARING("Shearing"), SIZE_SELECTION("Size Selection");

    /** The value. */
    private final String value;

    /**
     * Instantiates a new protocol type.
     *
     * @param value
     *          the value
     */
    PreLibraryPrepType(String value) {
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

  private Integer preLibraryPrepId;

  private PreLibraryPrepType preLibraryPrepType;

  private Double inputAmount;

  private Double targetDpSize;

  private Double averageFragmentSize;

  private Double concentration;

  private String quality;

  private String notes;

  private Protocol protocol;

  private Product product;

  private Timestamp lastModified;

  /**
   * Default Constructor.
   */
  public PreLibraryPrep() {
  }

  /**
   * Constructor needed to generate builder.
   * 
   * @param preLibraryPrepType
   * @param inputAmount
   * @param targetDpSize
   * @param averageFragmentSize
   * @param concentration
   * @param quality
   * @param notes
   * @param protocol
   * @param product
   */
  @Builder
  public PreLibraryPrep(PreLibraryPrepType preLibraryPrepType, Double inputAmount,
      Double targetDpSize, Double averageFragmentSize, Double concentration, String quality,
      String notes, Protocol protocol, Product product) {
    super();
    this.preLibraryPrepType = preLibraryPrepType;
    this.inputAmount = inputAmount;
    this.targetDpSize = targetDpSize;
    this.averageFragmentSize = averageFragmentSize;
    this.concentration = concentration;
    this.quality = quality;
    this.notes = notes;
    this.protocol = protocol;
    this.product = product;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PreLibraryPrepID")
  public Integer getPreLibraryPrepId() {
    return preLibraryPrepId;
  }

  public void setPreLibraryPrepId(Integer preLibraryPrepId) {
    this.preLibraryPrepId = preLibraryPrepId;
  }

  @NotNull
  @Type(type = "pgsql_enum")
  @Enumerated(EnumType.STRING)
  @Column(name = "PreLibraryPrepType")
  public PreLibraryPrepType getPreLibraryPrepType() {
    return preLibraryPrepType;
  }

  public void setPreLibraryPrepType(PreLibraryPrepType preLibraryPrepType) {
    this.preLibraryPrepType = preLibraryPrepType;
  }

  @Column(name = "InputAmount")
  public Double getInputAmount() {
    return inputAmount;
  }

  public void setInputAmount(Double inputAmount) {
    this.inputAmount = inputAmount;
  }

  @Column(name = "TargetDpSize")
  public Double getTargetDpSize() {
    return targetDpSize;
  }

  public void setTargetDpSize(Double targetDpSize) {
    this.targetDpSize = targetDpSize;
  }

  @Column(name = "AverageFragmentSize")
  public Double getAverageFragmentSize() {
    return averageFragmentSize;
  }

  public void setAverageFragmentSize(Double averageFragmentSize) {
    this.averageFragmentSize = averageFragmentSize;
  }

  @Column(name = "Concentration")
  public Double getConcentration() {
    return concentration;
  }

  public void setConcentration(Double concentration) {
    this.concentration = concentration;
  }

  @Column(name = "Quality")
  public String getQuality() {
    return quality;
  }

  public void setQuality(String quality) {
    this.quality = quality;
  }

  @Lob
  @Column(name = "Notes")
  @Type(type = "org.hibernate.type.TextType")
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @ManyToOne(cascade = {})
  @JoinColumn(name = "ProtocolID")
  public Protocol getProtocol() {
    return protocol;
  }

  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }

  @ManyToOne(cascade = {})
  @JoinColumn(name = "ProductID")
  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Column(name = "LastModified")
  public Timestamp getLastModified() {
    return lastModified;
  }

  public void setLastModified(Timestamp lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Since this implements UniqueObj, we need to set the standard way of retrieving the ID.
   */
  @Transient
  public Integer getId() {
    return getPreLibraryPrepId();
  }

  /**
   * Since this implements UniqueObj, we need to set the standard way of setting the ID.
   */
  public void setId(Integer id) {
    setPreLibraryPrepId(id);
  }

}
