package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne ;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "thermocycler_profile",
  uniqueConstraints = { @UniqueConstraint(columnNames = "Name") }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThermocyclerProfile implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private OffsetDateTime createdOn;

  @Column(name = "groupname")
  private String group;

  @NotNull
  @Size(max = 50)
  private String name;

  private String application;
  private String cycles;

  //max 15 elements with and each element are limited to 250 characters
  @Size(max = 15)
  private List<@Size(max = 250) String> steps;

  @Version
  private Timestamp lastModified;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "regionid")
  private Region region;

}
