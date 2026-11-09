package ca.gc.aafc.seqdb.api.entities;


import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ca.gc.aafc.dina.entity.DinaEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "molecular_analysis_result")
public class MolecularAnalysisResult implements DinaEntity {

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
  @Generated(event = EventType.INSERT)
  private OffsetDateTime createdOn;

  @NotBlank
  @Column(name = "_group")
  private String group;

  @Column(name = "attachments", columnDefinition = "uuid[]")
  @UniqueElements
  private List<UUID> attachments = List.of();

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "tabular_results", columnDefinition = "jsonb")
  @Valid
  private TabularResults tabularResults;

  /**
   * JSON document where each element of records is one row of the tabular data (column name to value).
   * <p>
   * Example:
   * <pre>{@code
   * {
   *   "records": [
   *       "region": 1,
   *       "start": 0,
   *       "end": 63513,
   *       "contig_edge": true,
   *       "product": "NRPS-like / betalactone / NRPS",
   *       "KCB_hit": "fengycin",
   *       "KCB_acc": "BGC0001095.5",
   *       "KCB_sim": "high",
   *       "record_desc": "gnl|C|OS0195F0_2 OS0195F0"
   *     }
   *   ]
   * }
   * }</pre>
   */
  public record TabularResults(@NotNull List<Map<String, Object>> records) implements Serializable {
  }

}
