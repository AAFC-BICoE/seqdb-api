package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
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
@Table(name = "LibraryPools")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPool {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;
  
  @NotNull
  private String name;
  
  private LocalDate dateUsed;
  
  private String notes;
  
  @Getter(onMethod=@__({
    @OneToMany(mappedBy = "libraryPool")
  }))
  private List<LibraryPoolContent> contents;

}
