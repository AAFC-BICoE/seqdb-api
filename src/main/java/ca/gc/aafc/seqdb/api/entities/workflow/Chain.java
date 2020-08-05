package ca.gc.aafc.seqdb.api.entities.workflow;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import ca.gc.aafc.seqdb.api.entities.Group;



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
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class Chain implements Serializable {

  private static final long serialVersionUID = -4501556733513556840L;
  private Integer chainId;
  private String name;
  private Date dateCreated;
  private ChainTemplate chainTemplate;
  private Group group;

  @Id
  @GeneratedValue(generator = "chain-sequence-generator")
  @GenericGenerator(name = "chain-sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
      @Parameter(name = "sequence_name", value = "chains_chainid_seq"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "1") })
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

  
  @NotNull
  @ManyToOne(cascade = {})
  @JoinColumn(name = "GroupID")
  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  @Transient
  public Integer getId() {
    return getChainId();
  }

  public void setId(Integer id) {
    setChainId(id);
  }

}
