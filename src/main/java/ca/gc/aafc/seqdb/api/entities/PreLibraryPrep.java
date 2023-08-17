package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

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
  @Type(type = "pgsql_enum")
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
