package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Builder;

@Entity

@Table(name = "IndexSets")
public class IndexSet {

  private Integer indexSetId;

  private String name;

  private String forwardAdapter;

  private String reverseAdapter;

  private List<NgsIndex> ngsIndexes;

  public IndexSet() {
  }

  @Builder
  public IndexSet(String name, String forwardAdapter, String reverseAdapter, List<NgsIndex> ngsIndexes) {
    super();
    this.name = name;
    this.forwardAdapter = forwardAdapter;
    this.reverseAdapter = reverseAdapter;
    this.ngsIndexes = ngsIndexes;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
