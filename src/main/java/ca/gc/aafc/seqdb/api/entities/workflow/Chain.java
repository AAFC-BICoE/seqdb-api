package ca.gc.aafc.seqdb.api.entities.workflow;

import java.sql.Date;

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
import javax.validation.constraints.Size;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * The chain entity is an instance of a workflow definition (aka. Chain Template). It also contains
 * meta data about the chain. A chain needs to have a chain template which describes the workflow
 * being performed, and a group which has access to the workflow instance.
 *
 */
@Entity

@Table(name = "Chains", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "ChainID", "ChainTemplateID" }) })
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Chain {

  private Integer chainId;
  private String name;
  private Date dateCreated;
  private ChainTemplate chainTemplate;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ChainID")
  public Integer getChainId() {
    return chainId;
  }

  public void setChainId(Integer chainId) {
    this.chainId = chainId;
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
  @Column(name = "DateCreated")
  public Date getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
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

  @Transient
  public Integer getId() {
    return getChainId();
  }

  public void setId(Integer id) {
    setChainId(id);
  }

}
