package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
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
public class IndexSet implements DinaEntity {

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
    @Column(name = "groupname")
    }))
  private String group;

  @NotNull
  private String name;
  
  private String forwardAdapter;
  
  private String reverseAdapter;
  
  @Getter(onMethod = @__({
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "indexSet")
    }))
  private List<NgsIndex> ngsIndexes;

}
