package ca.gc.aafc.seqdb.api.entities.workflow;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vladmihalcea.hibernate.type.array.EnumArrayType;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.StepTemplate.StepResourceValue;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Step Template describes a specific step in a specific workflow. Since a
 * workflow is similar to a pipeline, for each step an input and an output must
 * be specified or the workflow will break.
 * 
 * A step can also have an optional support step resource.
 */
@Entity
@Table(name = "StepTemplates")
@TypeDef(name = "step-resource-value-array", typeClass = EnumArrayType.class, defaultForType = StepResourceValue[].class, parameters = {
    @Parameter(name = EnumArrayType.SQL_ARRAY_TYPE, value = "stepresourcevalue") })
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepTemplate implements DinaEntity {

  public enum StepResourceValue {
    SAMPLE, PCR_BATCH, SEQ_BATCH, SEQ_SUBMISSION,
    SHEARING, SIZE_SELECTION, LIBRARY_PREP_BATCH, LIBRARY_POOL;
  }

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

  @Getter(onMethod = @__({
    @Column(name = "groupname")
    }))
  private String group;

  @NotNull
  @Size(max = 50)
  private String name;

  @NotNull
  @Type(type = "step-resource-value-array")
  private StepResourceValue[] inputs;
  
  @NotNull
  @Type(type = "step-resource-value-array")
  private StepResourceValue[] outputs;

  @Type(type = "step-resource-value-array")
  private StepResourceValue[] supports;

}
