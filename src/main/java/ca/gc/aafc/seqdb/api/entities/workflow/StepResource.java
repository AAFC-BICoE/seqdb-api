package ca.gc.aafc.seqdb.api.entities.workflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import ca.gc.aafc.seqdb.api.entities.Sample;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;
import lombok.Builder;

/**
 * The Step Resource is used to associate a specific data point to a given workflow step. This could
 * also be thought of as the step instance which corresponds to a step template.
 * 
 * The following constraints apply: - Only one foreign key can be used for a resource. (Specimen,
 * sample, etc...) - A Step Resource can only be linked to a specific step (ChainStepTemplate) if
 * the name of the foreign key being used exists in the corresponding input, output or support array
 * inside of the StepTemplate.
 */
@Entity

@Table(name = "StepResources")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class StepResource {

  private Integer stepResourceId;
  private StepResourceValue value;
  private ChainStepTemplate chainStepTemplate;
  private Chain chain;
  private Sample sample;
  private Product product;
  private PcrPrimer primer;
  private PcrProfile pcrProfile;
  private Protocol protocol;
  private PreLibraryPrep preLibraryPrep;
  private LibraryPrepBatch libraryPrepBatch;
  private LibraryPool libraryPool;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "StepResourceID")
  public Integer getStepResourceId() {
    return stepResourceId;
  }

  public void setStepResourceId(Integer stepResourceId) {
    this.stepResourceId = stepResourceId;
  }

  @NotNull
  @Column(name = "Value")
  @Enumerated(EnumType.STRING)
  @Type(type = "pgsql_enum")
  public StepResourceValue getValue() {
    return value;
  }

  public void setValue(StepResourceValue value) {
    this.value = value;
  }

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ChainID")
  public Chain getChain() {
    return chain;
  }

  public void setChain(Chain chain) {
    this.chain = chain;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SampleID")
  public Sample getSample() {
    return sample;
  }

  public void setSample(Sample sample) {
    this.sample = sample;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ProductID")
  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PrimerID")
  public PcrPrimer getPrimer() {
    return primer;
  }

  public void setPrimer(PcrPrimer primer) {
    this.primer = primer;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PcrProfileID")
  public PcrProfile getPcrProfile() {
    return pcrProfile;
  }

  public void setPcrProfile(PcrProfile pcrProfile) {
    this.pcrProfile = pcrProfile;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ProtocolID")
  public Protocol getProtocol() {
    return protocol;
  }

  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PreLibraryPrepID")
  public PreLibraryPrep getPreLibraryPrep() {
    return preLibraryPrep;
  }

  public void setPreLibraryPrep(PreLibraryPrep preLibraryPrep) {
    this.preLibraryPrep = preLibraryPrep;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LibraryPrepBatchID")
  public LibraryPrepBatch getLibraryPrepBatch() {
    return libraryPrepBatch;
  }

  public void setLibraryPrepBatch(LibraryPrepBatch libraryPrepBatch) {
    this.libraryPrepBatch = libraryPrepBatch;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LibraryPoolID")
  public LibraryPool getLibraryPool() {
    return libraryPool;
  }

  public void setLibraryPool(LibraryPool libraryPool) {
    this.libraryPool = libraryPool;
  }

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "chainTemplateId", referencedColumnName = "chainTemplateId", insertable = true, updatable = false, nullable = false),
      @JoinColumn(name = "stepTemplateId", referencedColumnName = "stepTemplateId", insertable = true, updatable = false, nullable = false) })
  
  public ChainStepTemplate getChainStepTemplate() {
    return chainStepTemplate;
  }

  public void setChainStepTemplate(ChainStepTemplate chainStepTemplate) {
    this.chainStepTemplate = chainStepTemplate;
  }

  @Transient
  public Integer getId() {
    return getStepResourceId();
  }

  public void setId(Integer id) {
    setStepResourceId(id);
  }

  public StepResource() {

  }

  /**
   * Instantiates a new StepResource.
   * 
   * @param stepResourceId
   *          the stepresource id
   * @param value
   *          the stepresource value
   * @param chainTemplate
   *          the chainStepTemplate
   * @param chain
   *          the workflow chain
   * @param specimen
   * @param specimenReplicate
   * @param mixedSpecimen
   * @param sample
   * @param pcrBatch
   * @param seqBatch
   * @param seqSubmission
   * @param product
   * @param primer
   * @param pcrProfile
   * @param protocol
   */
  @Builder
  public StepResource(StepResourceValue value, ChainStepTemplate chainStepTemplate, Chain chain,
      
      Sample sample,
      Product product, PcrPrimer primer, PcrProfile pcrProfile, Protocol protocol) {
    this.value = value;
    this.chainStepTemplate = chainStepTemplate;
    this.chain = chain;
    this.sample = sample;
    this.product = product;
    this.primer = primer;
    this.pcrProfile = pcrProfile;
    this.protocol = protocol;
  }

}
