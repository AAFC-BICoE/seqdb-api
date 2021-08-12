package ca.gc.aafc.seqdb.api.entities.sanger;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vladmihalcea.hibernate.type.array.ListArrayType;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.entities.Region;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@Builder
@Setter
@Getter
@RequiredArgsConstructor
@SuppressFBWarnings(justification = "ok for Hibernate Entity", value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
@NaturalIdCache
@Table(name = "pcr_batch")
@TypeDef(
  name = "list-array",
  typeClass = ListArrayType.class
)
public class PcrBatch implements DinaEntity {

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

  @Type(type = "list-array")
  @Column(name = "experimenters", columnDefinition = "uuid[]")
  private List<UUID> experimenters = Collections.emptyList();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "primer_forward_id")    
  private PcrPrimer primerForward;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "primer_reverse_id")
  private PcrPrimer primerReverse;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_id")
  private Region region;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "thermocycler_profile_id")
  private ThermocyclerProfile thermocyclerProfile;

  @Size(max = 50)
  private String thermocycler;

  @Size(max = 200)
  private String objective;

  @Column(name = "positive_control")
  @Size(max = 50)
  private String positiveControl;

  @Column(name = "reaction_volume")
  @Size(max = 50)
  private String reactionVolume;

  @Column(name = "reaction_date")
  private LocalDate reactionDate;

  @Type(type = "list-array")
  @Column(name = "attachment", columnDefinition = "uuid[]")
  private List<UUID> attachment = Collections.emptyList();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "container_type_id")
  private ContainerType containerType;
  
}