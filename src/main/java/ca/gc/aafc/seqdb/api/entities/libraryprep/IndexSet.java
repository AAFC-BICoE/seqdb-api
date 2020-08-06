package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import lombok.Builder;

@Entity

@Table(name = "IndexSets")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "SAGESDataCache")
public class IndexSet implements Serializable {

  private static final long serialVersionUID = -8370045494938799080L;

  private Integer indexSetId;

  private String name;

  private String forwardAdapter;

  private String reverseAdapter;

  private List<NgsIndex> ngsIndexes;

  public IndexSet() {
  }

  @Builder
  public IndexSet(String name, String forwardAdapter, String reverseAdapter,
      List<NgsIndex> ngsIndexes) {
    super();
    this.name = name;
    this.forwardAdapter = forwardAdapter;
    this.reverseAdapter = reverseAdapter;
    this.ngsIndexes = ngsIndexes;
  }

  @Id
  @GeneratedValue(generator = "index-set-sequence-generator")
  @GenericGenerator(name = "index-set-sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
      @Parameter(name = "sequence_name", value = "indexsets_indexsetid_seq"),
      @Parameter(name = "initial_value", value = "1"),
      @Parameter(name = "increment_size", value = "1") })
  @Column(name = "IndexSetID")
  public Integer getIndexSetId() {
    return indexSetId;
  }

  public void setIndexSetId(Integer indexSetId) {
    this.indexSetId = indexSetId;
  }

  @NotNull
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "ForwardAdapter")
  public String getForwardAdapter() {
    return forwardAdapter;
  }

  public void setForwardAdapter(String forwardAdapter) {
    this.forwardAdapter = forwardAdapter;
  }

  @Column(name = "ReverseAdapter")
  public String getReverseAdapter() {
    return reverseAdapter;
  }

  public void setReverseAdapter(String reverseAdapter) {
    this.reverseAdapter = reverseAdapter;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "indexSet")
  public List<NgsIndex> getNgsIndexes() {
    return this.ngsIndexes;
  }

  public void setNgsIndexes(List<NgsIndex> ngsIndexes) {
    this.ngsIndexes = ngsIndexes;
  }

  @Transient
  public Integer getId() {
    return indexSetId;
  }

  public void setId(Integer id) {
    this.indexSetId = id;
  }

}
