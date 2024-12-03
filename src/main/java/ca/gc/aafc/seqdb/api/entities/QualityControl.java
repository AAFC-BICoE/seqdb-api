package ca.gc.aafc.seqdb.api.entities;

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
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;

@Getter
@Entity
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quality_control")
public class QualityControl implements DinaEntity {

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

  @Column(name = "_group")
  private String group;

  @NotBlank
  @Size(max = 100)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Column(name = "qc_type")
  private String qcType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "molecular_analysis_run_item_id")
  private MolecularAnalysisRunItem molecularAnalysisRunItem;

}
