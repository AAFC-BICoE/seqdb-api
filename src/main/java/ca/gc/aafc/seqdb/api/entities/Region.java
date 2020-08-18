package ca.gc.aafc.seqdb.api.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class Region {

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @NotNull
  @Size(max = 50)
  private String symbol;

  private String name;

  private String description;
  private String aliases;
  private String applicableOrganisms;

}
