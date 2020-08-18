package ca.gc.aafc.seqdb.api.entities.workflow;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
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
@AllArgsConstructor
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Chain {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @NotNull
  @Size(max = 50)
  private String name;

  @NotNull
  private Date dateCreated;

  @Getter(onMethod=@__({
    @NotNull,
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn
  }))
  private ChainTemplate chainTemplate;

}
