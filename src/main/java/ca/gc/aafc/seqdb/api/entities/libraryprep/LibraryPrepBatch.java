package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.PcrProfile;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A batch of library preps for samples.
 */
@Entity
@Table(name = "LibraryPrepBatchs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPrepBatch {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @Getter(onMethod=@__({
    @NotNull,
    @NaturalId
  }))
  private UUID uuid;

  @NotNull
  private String name;

  private Double totalLibraryYieldNm;

  private String notes;

  private String cleanUpNotes;

  private String yieldNotes;

  private LocalDate dateUsed;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "productid")
  }))
  private Product product;
  
  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "protocolid")
  }))
  private Protocol protocol;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "containertypeid")
  }))
  private ContainerType containerType;
  
  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "thermocyclerprofileid")
  }))
  private PcrProfile thermocyclerProfile;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "indexsetid")
  }))
  private IndexSet indexSet;
  
  @Getter(onMethod=@__({
    @OneToMany(mappedBy = "libraryPrepBatch")
  }))
  private List<LibraryPrep> libraryPreps;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
