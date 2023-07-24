package ca.gc.aafc.seqdb.api.entities.libraryprep;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LibraryPreps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryPrep implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  private Double inputNg;

  private String quality;

  private String size;

  @Min(value = 1)
  private Integer wellColumn;

  @Size(max = 2)
  @Pattern(regexp = "[a-zA-Z]")
  private String wellRow;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "libraryprepbatchid")
  private LibraryPrepBatch libraryPrepBatch;
  
  @NotNull
  @Column(name = "material_sample")
  private UUID materialSample;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Indexi5id")
  private NgsIndex indexI5;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "indexi7id")
  private NgsIndex indexI7;

  @NotBlank
  @Column(name = "_group")
  private String group;


}
