package ca.gc.aafc.seqdb.api.entities;

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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

  @Size(max = 150)
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
