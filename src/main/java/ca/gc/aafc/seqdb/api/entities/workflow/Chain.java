package ca.gc.aafc.seqdb.api.entities.workflow;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * The chain entity is an instance of a workflow definition (aka. Chain Template). It also contains
 * meta data about the chain. A chain needs to have a chain template which describes the workflow
 * being performed, and a group which has access to the workflow instance.
 *
 */
@Entity
@Table(name = "Chains")
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Chain implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @Column(name = "groupname")
  private String group;

  @NotNull
  @Size(max = 50)
  private String name;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chaintemplateid")
  private ChainTemplate chainTemplate;

}
