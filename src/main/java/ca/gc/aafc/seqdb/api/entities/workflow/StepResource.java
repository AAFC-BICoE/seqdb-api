package ca.gc.aafc.seqdb.api.entities.workflow;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.Sample;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
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
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepResource {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @Getter(onMethod=@__({
    @NotNull,
    @NaturalId
  }))
  private UUID uuid;

  @NotNull
  @Column(name = "Value")
  @Enumerated(EnumType.STRING)
  @Type(type = "pgsql_enum")
  private StepResourceValue value;

  @Getter(onMethod=@__({
    @NotNull,
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumns({
      @JoinColumn(name = "chainTemplateId", referencedColumnName = "chainTemplateId", updatable = false),
      @JoinColumn(name = "stepTemplateId", referencedColumnName = "stepTemplateId", updatable = false) })
  }))
  private ChainStepTemplate chainStepTemplate;

  @Getter(onMethod=@__({
    @NotNull,
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "chainid")
  }))
  private Chain chain;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "sampleid")
  }))
  private Sample sample;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "prelibraryprepid")
  }))
  private PreLibraryPrep preLibraryPrep;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "libraryprepbatchid")
  }))
  private LibraryPrepBatch libraryPrepBatch;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "librarypoolid")
  }))
  private LibraryPool libraryPool;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
