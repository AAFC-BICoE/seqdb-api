package ca.gc.aafc.seqdb.api.entities.workflow;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * Chain Template is the definition of a workflow. It is the blueprint to how a workflow is
 * designed. This could also be known as the workflow type, such as SANGER or shotgun.
 *
 * When creating a brand new workflow instance, you need to select what type of workflow you are
 * creating. This entity is the type end users would select.
 */
@Entity
@Table(name = "ChainTemplates")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class ChainTemplate implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer chainTemplateId;
  private String name;

  @Id
  @GeneratedValue(generator = "chain-template-sequence-generator")
  @GenericGenerator(name = "chain-template-sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
      @Parameter(name = "sequence_name", value = "chaintemplates_chaintemplateid_seq"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "1") })
  @Column(name = "ChainTemplateID")
  public Integer getChainTemplateId() {
    return chainTemplateId;
  }

  public void setChainTemplateId(Integer chainTemplateId) {
    this.chainTemplateId = chainTemplateId;
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

  @Transient
  public Integer getId() {
    return getChainTemplateId();
  }

  public void setId(Integer id) {
    setChainTemplateId(id);
  }

}
