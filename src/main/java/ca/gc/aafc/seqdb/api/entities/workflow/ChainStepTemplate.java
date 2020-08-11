package ca.gc.aafc.seqdb.api.entities.workflow;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Builder;

/**
 * Since a workflow needs to have a specific set of steps (in a specific order), this is how steps
 * are ordered. The chain step template will link to a Chain template (The workflow definition) and
 * a Step Template (the actual step to perform) and assign a step number. Without this entity, there
 * would be no way to indicate what step is first, second, third, etc...
 */
@Entity
@Table(name = "ChainStepTemplates", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "ChainTemplateID", "StepNumber" }),
    @UniqueConstraint(columnNames = { "ChainTemplateID", "StepTemplateID" }) })
public class ChainStepTemplate implements Serializable {

  private static final long serialVersionUID = -3484692979076302405L;
  private Integer chainStepTemplateId;
  private ChainTemplate chainTemplate;
  private StepTemplate stepTemplate;
  private Integer stepNumber;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ChainStepTemplateID")
  public Integer getChainStepTemplateId() {
    return chainStepTemplateId;
  }

  public void setChainStepTemplateId(Integer chainStepTemplateId) {
    this.chainStepTemplateId = chainStepTemplateId;
  }

  @NotNull
  @ManyToOne(cascade = {})
  @JoinColumn(name = "ChainTemplateID")
  public ChainTemplate getChainTemplate() {
    return chainTemplate;
  }

  public void setChainTemplate(ChainTemplate chainTemplate) {
    this.chainTemplate = chainTemplate;
  }

  @NotNull
  @ManyToOne(cascade = {})
  @JoinColumn(name = "StepTemplateID")
  public StepTemplate getStepTemplate() {
    return stepTemplate;
  }

  public void setStepTemplate(StepTemplate stepTemplate) {
    this.stepTemplate = stepTemplate;
  }

  @NotNull
  @Column(name = "StepNumber")
  public Integer getStepNumber() {
    return stepNumber;
  }

  public void setStepNumber(Integer stepNumber) {
    this.stepNumber = stepNumber;
  }

  @Transient
  public Integer getId() {
    return getChainStepTemplateId();
  }

  public void setId(Integer id) {
    setChainStepTemplateId(id);
  }

  // Constructors

  /**
   * Instantiates a new chain
   */

  public ChainStepTemplate() {

  }

  /**
   * Instantiates a new ChainStepTemplate
   * 
   * @param chainTemplate
   *          the chainsteptemplate
   * @param stepTemplate
   *          the steptempalte
   * @param stepNumber
   *          the step number on the specified steptemplate for current chain/workflow
   */
  @Builder
  public ChainStepTemplate(ChainTemplate chainTemplate, StepTemplate stepTemplate,
      Integer stepNumber) {
    super();
    this.chainTemplate = chainTemplate;
    this.stepTemplate = stepTemplate;
    this.stepNumber = stepNumber;
  }

}
