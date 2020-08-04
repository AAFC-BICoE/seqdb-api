package ca.gc.aafc.seqdb.api.entities.workflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vladmihalcea.hibernate.type.array.EnumArrayType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;
import lombok.Builder;

/**
 * Step Template describes a specific step in a specific workflow. Since a workflow is similar to a
 * pipeline, for each step an input and an output must be specified or the workflow will break.
 * 
 * A step can also have an optional support step resource.
 */
@Entity
@Table(name = "StepTemplates")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
@TypeDef(name = "step-resource-value-array", typeClass = EnumArrayType.class, defaultForType = StepResourceValue[].class, parameters = {
    @Parameter(name = EnumArrayType.SQL_ARRAY_TYPE, value = "stepresourcevalue") })
public class StepTemplate {

  public StepTemplate() {

  }

  /**
   * Builder constructor.
   * 
   * @param stepTemplateId
   * @param name
   * @param inputs
   * @param outputs
   * @param supports
   */
  @Builder
  public StepTemplate(String name, StepResourceValue[] inputs, StepResourceValue[] outputs,
      StepResourceValue[] supports) {
    this.name = name;
    this.inputs = inputs;
    this.outputs = outputs;
    this.supports = supports;
  }

  public StepTemplate(String name, StepResourceValue[] inputs, StepResourceValue[] outputs) {
    this.name = name;
    this.inputs = inputs;
    this.outputs = outputs;
  }

  public enum StepResourceValue {
    SPECIMEN, SPECIMEN_REPLICATE, MIXED_SPECIMEN, SAMPLE, PCR_BATCH, SEQ_BATCH, SEQ_SUBMISSION, PRODUCT, REGION, PRIMER, PCR_PROFILE, PROTOCOL, SHEARING, SIZE_SELECTION, LIBRARY_PREP_BATCH, LIBRARY_POOL;
  }

  private Integer stepTemplateId;
  private String name;
  private StepResourceValue[] inputs;
  private StepResourceValue[] outputs;
  private StepResourceValue[] supports;

  @Id
  @GeneratedValue(generator = "step-template-sequence-generator")
  @GenericGenerator(name = "step-template-sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
      @Parameter(name = "sequence_name", value = "steptemplates_steptemplateid_seq"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "1") })
  @Column(name = "StepTemplateID")
  public Integer getStepTemplateId() {
    return stepTemplateId;
  }

  public void setStepTemplateId(Integer stepTemplateId) {
    this.stepTemplateId = stepTemplateId;
  }

  @NotNull
  @Size(max = 50)
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @NotNull
  @Column(name = "Inputs")
  @Type(type = "step-resource-value-array")
  public StepResourceValue[] getInputs() {
    return inputs;
  }

  public void setInputs(StepResourceValue[] inputs) {
    this.inputs = inputs;
  }

  @NotNull
  @Column(name = "Outputs")
  @Type(type = "step-resource-value-array")
  public StepResourceValue[] getOutputs() {
    return outputs;
  }

  public void setOutputs(StepResourceValue[] outputs) {
    this.outputs = outputs;
  }

  @Column(name = "Supports")
  @Type(type = "step-resource-value-array")
  public StepResourceValue[] getSupports() {
    return supports;
  }

  public void setSupports(StepResourceValue[] supports) {
    this.supports = supports;
  }

  @Transient
  public Integer getId() {
    return getStepTemplateId();
  }

  public void setId(Integer id) {
    setStepTemplateId(id);
  }

}
