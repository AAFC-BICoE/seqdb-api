package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.type.SqlTypes;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class for PreLibraryPrep.
 * 
 * The Pre-Library Prep entity is used in a workflow step to record shearing or size selection
 * performed on a Sample.
 *
 */
@Entity
@Table(name = "PreLibraryPreps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreLibraryPrep implements DinaEntity {

  @AllArgsConstructor
  public enum PreLibraryPrepType {
    SHEARING("Shearing"),
    SIZE_SELECTION("Size Selection");

    @Getter
    private final String value;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull
  @NaturalId
  private UUID uuid;

  private String createdBy;

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @NotNull
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  private PreLibraryPrepType preLibraryPrepType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "library_prep_id")
  private LibraryPrep libraryPrep;

  private Double inputAmount;

  private Double targetBpSize;

  private Double averageFragmentSize;

  private Double concentration;

  private String quality;

  private String notes;

  private UUID protocol;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "productid")
  private Product product;

  @Version
  private Timestamp lastModified;

  @NotBlank
  @Column(name = "_group")
  private String group;

}
