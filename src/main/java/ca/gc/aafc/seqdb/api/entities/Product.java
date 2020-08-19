package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products", uniqueConstraints = {
@UniqueConstraint(columnNames = { "Name", "UPC" }) })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Product {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @Getter(onMethod=@__({
    @NotNull,
    @NaturalId
  }))
  private UUID uuid;

  @NotNull
  @Size(max = 50)
  private String name;

  @Size(max = 50)
  private String upc;

  @Size(max = 50)
  private String type;

  private String description;

  @Getter(onMethod=@__({
    @Version
  }))
  private Timestamp lastModified;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
