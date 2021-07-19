package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import ca.gc.aafc.seqdb.api.entities.Product;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import ca.gc.aafc.seqdb.api.entities.workflow.Chain;
import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class LibraryPrepBatch implements DinaEntity {

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

  private Double totalLibraryYieldNm;

  private String notes;

  private String cleanUpNotes;

  private String yieldNotes;

  private LocalDate dateUsed;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "productid")
    }))
  private Product product;
  
  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "protocolid")
    }))
  private Protocol protocol;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "containertypeid")
    }))
  private ContainerType containerType;
  
  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "thermocyclerprofileid")
    }))
  private ThermocyclerProfile thermocyclerProfile;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "indexsetid")
    }))
  private IndexSet indexSet;
  
  @Getter(onMethod = @__({
    @OneToMany(mappedBy = "libraryPrepBatch", fetch = FetchType.LAZY)
    }))
  private List<LibraryPrep> libraryPreps;

  @Getter(onMethod = @__({
    @OneToOne(mappedBy = "libraryPrepBatch", fetch = FetchType.LAZY)
    }))
  @EqualsAndHashCode.Exclude
  private StepResource stepResource;

  @Getter(onMethod = @__({
    @NotBlank,
    @Column(name = "_group")
    }))
  private String group;

}
