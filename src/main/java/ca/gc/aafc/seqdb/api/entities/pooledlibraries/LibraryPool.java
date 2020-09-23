package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

import java.time.LocalDate;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LibraryPools")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPool implements DinaEntity {

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
  
  @NotNull
  private String name;
  
  private LocalDate dateUsed;
  
  private String notes;

  @Getter(onMethod = @__({
    @OneToOne(mappedBy = "libraryPool", fetch = FetchType.LAZY)
    }))
  private StepResource stepResource;
  
  @Getter(onMethod = @__({
    @OneToMany(mappedBy = "libraryPool", fetch = FetchType.LAZY)
    }))
  private List<LibraryPoolContent> contents;

  @Transient
  @Override
  public String getGroup() {
    return this.getStepResource().getGroup();
  }

}
