package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import ca.gc.aafc.dina.entity.DinaEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "ContainerTypes",
  uniqueConstraints = { @UniqueConstraint(columnNames = "Name") }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContainerType implements DinaEntity {

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

  @NotNull
  private Integer numberOfColumns;

  @NotNull
  private Integer numberOfRows;

  @Version
  private Timestamp lastModified;

  /**
   * Simple validator that checks the argument integers against the containerType's rows and column
   * to see if they mismatch
   * 
   * @param columnnumber
   * @param rownumber
   * @return true if the number of rows and columns in the container is more than the columnnumber
   *         and rownumber
   */
  public boolean isValidLocation(int columnnumber, int rownumber) {
    return getNumberOfColumns() >= columnnumber && getNumberOfRows() >= rownumber;
  }
}
