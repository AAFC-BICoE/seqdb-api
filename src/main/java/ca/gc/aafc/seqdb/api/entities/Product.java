package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "products",
  uniqueConstraints = { @UniqueConstraint(columnNames = { "Name", "UPC" }) }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements DinaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  @Column(name = "createdby")
  private String createdBy;

  @Column(name = "createdon", insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @Column(name = "groupname")
  private String group;

  @NotNull
  @Size(max = 50)
  private String name;

  private String upc;

  private String type;

  private String description;

  @Version
  @Column(name = "lastmodified")
  private Timestamp lastModified;

}
