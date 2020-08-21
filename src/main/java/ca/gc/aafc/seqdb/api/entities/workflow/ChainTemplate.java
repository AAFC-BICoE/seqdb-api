package ca.gc.aafc.seqdb.api.entities.workflow;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Chain Template is the definition of a workflow. It is the blueprint to how a workflow is
 * designed. This could also be known as the workflow type, such as SANGER or shotgun.
 *
 * When creating a brand new workflow instance, you need to select what type of workflow you are
 * creating. This entity is the type end users would select.
 */
@Entity
@Table(name = "ChainTemplates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChainTemplate {

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

  @NotNull
  @Size(max = 50)
  private String name;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
