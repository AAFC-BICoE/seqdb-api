package ca.gc.aafc.seqdb.api.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The Class Region.
 */
@Entity
@Table(name = "Regions", uniqueConstraints = {
    @UniqueConstraint(columnNames = "Name") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region implements DinaEntity {

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

  @Getter(onMethod = @__({
    @Column(insertable = false, updatable = false),
    @Generated(value = GenerationTime.INSERT)
    })) 
  private OffsetDateTime createdOn;

  @Getter(onMethod = @__({
    @Column(name = "groupname")
    }))
  private String group;

  @NotNull
  @Size(max = 50)
  private String symbol;

  private String name;

  private String description;
  private String aliases;
  private String applicableOrganisms;

}
