package ca.gc.aafc.seqdb.api.entities.workflow;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepResource implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @NotNull
  @Column(name = "Value")
  @Enumerated(EnumType.STRING)
  @Type(type = "pgsql_enum")
  private StepResourceValue value;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(name = "chainTemplateId", referencedColumnName = "chainTemplateId", updatable = false),
    @JoinColumn(name = "stepTemplateId", referencedColumnName = "stepTemplateId", updatable = false) 
  })
  private ChainStepTemplate chainStepTemplate;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chainid")
  private Chain chain;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sampleid")
  private MolecularSample molecularSample;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "libraryprepbatchid")
  private LibraryPrepBatch libraryPrepBatch;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "librarypoolid")
  private LibraryPool libraryPool;

  @Transient
  @Override
  public String getGroup() {
    return this.getChain().getGroup();
  }

}
