package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Optional;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.seqdb.api.entities.workflow.Chain;
import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class PreLibraryPrep implements DinaEntity {

  @AllArgsConstructor
  public enum PreLibraryPrepType {
    SHEARING("Shearing"),
    SIZE_SELECTION("Size Selection");

    @Getter
    private final String value;
  }

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

  @Column(insertable = false, updatable = false)
  private OffsetDateTime createdOn;

  @Getter(onMethod = @__({
    @NotNull,
    @Type(type = "pgsql_enum"),
    @Enumerated(EnumType.STRING)
    }))
  private PreLibraryPrepType preLibraryPrepType;

  private Double inputAmount;

  private Double targetDpSize;

  private Double averageFragmentSize;

  private Double concentration;

  private String quality;

  private String notes;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "protocolid")
    }))
  private Protocol protocol;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "productid")
    }))
  private Product product;
  
  @Getter(onMethod = @__({
    @OneToOne(mappedBy = "preLibraryPrep", fetch = FetchType.LAZY)
    }))
  private StepResource stepResource;

  @Getter(onMethod = @__({
    @Version
    }))
  private Timestamp lastModified;


  @Transient
  @Override
  public String getGroup() {
    return Optional.ofNullable(this.getStepResource())
      .map(StepResource::getChain)
      .map(Chain::getGroup)
      .orElse(null);
  }

}
