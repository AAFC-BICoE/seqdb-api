package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = "Samples", uniqueConstraints = { @UniqueConstraint(columnNames = { "Name", "Version" }) }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Sample {

  @AllArgsConstructor
  public enum SampleType {
    NO_TYPE("No Sample Type"),
    DNA("DNA"),
    RNA("RNA"),
    PROTEIN("Protein"),
    LIBRARY("Library"),
    CHEMICAL("Chemical");

    @Getter
    private final String value;

    /**
     * Returns the Sample Type ( Wrapped in an Optional )based off a case
     * insensitive match of its value. Or an empty Optional if a match could not be
     * made.
     * 
     * @param searchValue - sample type to search
     * @return - the sample type or empty wrapped in an Optional
     */
    public static Optional<SampleType> getByValue(String searchValue) {
      for (SampleType type : SampleType.values()) {
        if (StringUtils.equalsIgnoreCase(type.getValue(), searchValue)) {
          return Optional.of(type);
        }
      }
      return Optional.empty();
    }
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

  @NotNull
  @Size(max = 50)
  private String name;

  @NotNull
  @Size(max = 50)
  private String version;

  private String notes;

  @Getter(onMethod = @__({
    @Version
    }))
  private Timestamp lastModified;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "productid")
    }))
  private Product kit;

  @Getter(onMethod = @__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "protocolid")
    }))
  private Protocol protocol;

  private String discardedNotes;
  private LocalDate dateDiscarded;

  @Enumerated(EnumType.STRING)
  private SampleType sampleType;

  @PrePersist
  public void prePersist() {
    this.uuid = UUID.randomUUID();
  }

}
