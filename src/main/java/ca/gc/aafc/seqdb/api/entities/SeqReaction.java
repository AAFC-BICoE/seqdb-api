package ca.gc.aafc.seqdb.api.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Table(name = "seq_reaction")
public class SeqReaction implements DinaEntity {

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
  @JoinColumn(name = "seq_batch_id")
  private SeqBatch seqBatch;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pcr_batch_item_id")
  private PcrBatchItem pcrBatchItem;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seq_primer_id")
  private PcrPrimer seqPrimer;

  @Column(name = "storage_unit_usage")
  private UUID storageUnitUsage;

}
