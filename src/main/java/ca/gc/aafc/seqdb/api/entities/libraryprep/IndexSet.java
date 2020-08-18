package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "IndexSets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndexSet {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;
  
  @NotNull
  private String name;
  
  private String forwardAdapter;
  
  private String reverseAdapter;
  
  @Getter(onMethod=@__({
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "indexSet")
  }))
  private List<NgsIndex> ngsIndexes;

}
