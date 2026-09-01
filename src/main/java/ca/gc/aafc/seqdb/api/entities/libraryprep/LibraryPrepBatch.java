package ca.gc.aafc.seqdb.api.entities.libraryprep;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import ca.gc.aafc.seqdb.api.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A batch of library preps for samples.
 */
@Entity
@Table(name = "libraryprepbatchs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPrepBatch implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  @Column(name = "createdby")
  private String createdBy;

  @Column(name = "createdon", insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @NotNull
  private String name;

  @Column(name = "totallibraryyieldnm")
  private Double totalLibraryYieldNm;

  private String notes;
  
  @Column(name = "cleanupnotes")
  private String cleanUpNotes;

  @Column(name = "yieldnotes")
  private String yieldNotes;

  @Column(name = "dateused")
  private LocalDate dateUsed;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productid")
  private Product product;
  
  private UUID protocol;

  @Column(name = "storage_unit")
  private UUID storageUnit;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "thermocyclerprofileid")
  private ThermocyclerProfile thermocyclerProfile;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "indexsetid")
  private IndexSet indexSet;
  
  @OneToMany(mappedBy = "libraryPrepBatch", fetch = FetchType.LAZY)
  private List<LibraryPrep> libraryPreps;

  @NotBlank
  @Column(name = "_group")
  private String group;

}
