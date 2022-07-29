package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne ;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Table(
  name = "thermocycler_profile",
  uniqueConstraints = { @UniqueConstraint(columnNames = "Name") }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class ThermocyclerProfile implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @Column(name = "groupname")
  private String group;

  @NotNull
  @Size(max = 50)
  private String name;

  private String application;
  private String cycles;

  //max 15 elements with and each element are limited to 250 characters
  @Size(max=15)
  @Type(type = "list-array")
  private List<@Size(max=250) String> steps;

  @Version
  private Timestamp lastModified;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "regionid")
  private Region region;

}
