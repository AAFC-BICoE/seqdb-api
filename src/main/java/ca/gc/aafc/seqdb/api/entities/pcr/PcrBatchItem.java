package ca.gc.aafc.seqdb.api.entities.pcr;

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
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@RequiredArgsConstructor
@NaturalIdCache
@Table(name = "pcr_batch_item")
public class PcrBatchItem implements DinaEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NaturalId
  @NotNull
  @Column(unique = true)
  private UUID uuid;

  @NotBlank
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @Column(name = "created_on", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @NotBlank
  @Column(name = "_group")
  private String group;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pcr_batch_id")
  private PcrBatch pcrBatch;

  @Column(name = "material_sample")
  private UUID materialSample;

  @Min(value = 1)
  @Max(value = 255)
  @Column(name = "well_column")
  private Integer wellColumn;

  @Size(max = 2)
  @Pattern(regexp = "[a-zA-Z]")
  @Column(name = "well_row")
  private String wellRow;

  // calculated field
  @Transient
  private int cellNumber;

  @Size(max = 255)
  private String result;

}
