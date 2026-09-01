package ca.gc.aafc.seqdb.api.entities.pooledlibraries;

import java.time.LocalDate;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "librarypools")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPool implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  @Column(name = "createdby", updatable = false)
  private String createdBy;

  @Column(name = "createdon", insertable = false, updatable = false)
  private OffsetDateTime createdOn;
  
  @NotNull
  private String name;
  
  @Column(name = "dateused")
  private LocalDate dateUsed;
  
  private String notes;

  @OneToMany(mappedBy = "libraryPool", fetch = FetchType.LAZY)
  private List<LibraryPoolContent> contents;

  @NotBlank
  @Column(name = "_group")
  private String group;


}
