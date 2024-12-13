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
import javax.validation.constraints.NotEmpty;
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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "molecular_analysis_run_item")
public class MolecularAnalysisRunItem implements DinaEntity {

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

  @NotEmpty
  @Size(max = 50)
  @Column(name = "usage_type")
  private String usageType;

  @Size(max = 50)
  private String name;

  // eager since we need it for group-based permission
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "molecular_analysis_run_id")
  private MolecularAnalysisRun run;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "molecular_analysis_result_id")
  private MolecularAnalysisResult result;

  @Override
  public String getGroup() {
    if (run == null) {
      return null;
    }
    return run.getGroup();
  }

}
