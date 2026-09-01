package ca.gc.aafc.seqdb.api.entities.libraryprep;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;

import java.time.OffsetDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "librarypreps")
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

  @Column(name = "createdby", updatable = false)
  private String createdBy;

  @Column(name = "createdon", insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @Column(name = "inputng")
  private Double inputNg;

  private String quality;

  private String size;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "libraryprepbatchid")
  private LibraryPrepBatch libraryPrepBatch;

  @Column(name = "storage_unit_usage")
  private UUID storageUnitUsage;

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
