package ca.gc.aafc.seqdb.api.entities.workflow;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings(
  value = "SE_BAD_FIELD",
  justification = "ChainStepTemplate must implement Serializable or else ManyToOneType.hydrate fails when linking StepResources to a ChainStepTemplate.")
public class ChainStepTemplate implements DinaEntity, Serializable {

  private static final long serialVersionUID = -5794571772289124902L;

  @Getter(onMethod = @__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    }))
  private Integer id;

  @Getter(onMethod = @__({
    @NotNull,
    @NaturalId
    }))
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;
  
  @Getter(onMethod = @__({
    @NotNull,
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "chaintemplateid")
    }))
  private ChainTemplate chainTemplate;
  
  @Getter(onMethod = @__({
    @NotNull,
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "steptemplateid")
    }))
  private StepTemplate stepTemplate;

  @NotNull
  private Integer stepNumber;

  @Transient
  @Override
  public String getGroup() {
    return this.getChainTemplate().getGroup();
  }

}
