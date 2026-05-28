package ca.gc.aafc.seqdb.api.entities;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
import org.hibernate.annotations.Type;

import ca.gc.aafc.dina.entity.DinaEntity;

@Getter
@Entity
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "generic_molecular_analysis")
public class GenericMolecularAnalysis implements DinaEntity {

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

  private UUID protocol;

  @NotBlank
  @Size(max = 50)
  private String analysisType;

  @Type(JsonType.class)
  @NotNull
  @Builder.Default
  @Column(name = "managed_attributes", columnDefinition = "jsonb")
  private Map<String, String> managedAttributes = Map.of();

}
