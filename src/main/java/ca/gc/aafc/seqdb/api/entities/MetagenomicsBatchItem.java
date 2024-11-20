package ca.gc.aafc.seqdb.api.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex;

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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metagenomics_batch_item")
public class MetagenomicsBatchItem implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  @NotBlank
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @Column(name = "created_on", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  // eager since we need it for group-based permission
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "metagenomics_batch_id")
  private MetagenomicsBatch metagenomicsBatch;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "indexi5_id")
  private NgsIndex indexI5;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "indexi7_id")
  private NgsIndex indexI7;

  @Override
  public String getGroup() {
    if (metagenomicsBatch == null) {
      return null;
    }
    return metagenomicsBatch.getGroup();
  }

}
