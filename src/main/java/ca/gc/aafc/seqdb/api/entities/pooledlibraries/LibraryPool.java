package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

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
public class LibraryPool {

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
  
  @NotNull
  private String name;
  
  private LocalDate dateUsed;
  
  private String notes;
  
  @Getter(onMethod = @__({
    @OneToMany(mappedBy = "libraryPool")
    }))
  private List<LibraryPoolContent> contents;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
