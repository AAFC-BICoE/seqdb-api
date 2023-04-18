package ca.gc.aafc.seqdb.api.entities;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;

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
@Table(name = "seq_batch")
public class SeqBatch implements DinaEntity {

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

  @NotBlank
  @Size(max = 100)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Column(name = "sequencing_type")
  private String sequencingType;

  @Column(name = "reaction_date")
  private LocalDate reactionDate;

  @Type(type = "list-array")
  @Column(name = "experimenters", columnDefinition = "uuid[]")
  private List<UUID> experimenters = Collections.emptyList();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "thermocycler_profile_id")
  private ThermocyclerProfile thermocyclerProfile;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_id")
  private Region region;
  
  private UUID protocol;

  @Column(name = "storage_unit")
  private UUID storageUnit;

  /**
   * storage-unit-type should only be used when no specific storageUnit is used.
   */
  @Column(name = "storage_unit_type")
  private UUID storageUnitType;

  @Type(type = "jsonb")
  @Column(name = "storage_restriction", columnDefinition = "jsonb")
  @Valid
  private StorageRestriction storageRestriction;
}
