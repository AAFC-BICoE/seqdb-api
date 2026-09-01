package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "indexsets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndexSet implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  @Column(name = "createdby", updatable = false)
  private String createdBy;

  @Column(name = "createdon", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @Column(name = "groupname")
  private String group;

  @NotNull
  private String name;
  
  @Column(name = "forwardadapter")
  private String forwardAdapter;

  @Column(name = "reverseadapter")
  private String reverseAdapter;
  
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "indexSet")
  private List<NgsIndex> ngsIndexes;

}
