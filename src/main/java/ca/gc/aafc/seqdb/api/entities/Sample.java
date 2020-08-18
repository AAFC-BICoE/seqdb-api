package ca.gc.aafc.seqdb.api.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Samples", uniqueConstraints = {
@UniqueConstraint(columnNames = { "Name", "Version" }) })
@Data
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
     * Returns the Sample Type ( Wrapped in an Optional )based off a case insensitive match of its
     * value. Or an empty Optional if a match could not be made.
     * 
     * @param searchValue
     *          - sample type to search
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

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @NotNull
  @Size(max = 50)
  private String name;

  @Size(max = 50)
  private String experimenter;

  @NotNull
  @Size(max = 50)
  private String version;
  private String versionDescription;
  private String treatment;
  private String source;

  @Size(max = 50)
  private String dnaConcentration;
  private String dnaConcentrationNotes;

  @Size(max = 50)
  private String dnaConcentrationPerStartMaterial;
  private Date date;

  @Size(max = 50)
  private String nuclAcidExt;

  private String extractionBatch;
  private Double pelletSize;
  private Double lysisBufferVolume;
  private Double proteinaseKVolume;
  private Double qubitDNAConcentration;
  private String quantificationMethod;

  @Size(max = 50)
  private String growthMedia;

  private String dnaNotes;
  private String notes;

  @Size(max = 50)
  private String tubeId;

  private Boolean unusableDna;
  private Timestamp lastModified;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "protocol_id", referencedColumnName = "id")
  }))
  private Product kit;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn
  }))
  private Protocol protocol;

  private String discardedNotes;
  private LocalDate dateDiscarded;

  @Column(name = "SampleType")
  @Enumerated(EnumType.STRING)
  private SampleType sampleType;

  private LocalDate inoculationDate;

  @Size(max = 50)
  private String fraction;

  private Double fermentationTemperature;

  @Size(max = 50)
  private String fermentationTime;

  @Size(max = 50)
  private String extractionSolvent;
  private String dateArchived;

  @Size(max = 50)
  private String sampleStorageLocation;  
  
  @Size(max = 50)
  private String sampleStorageConditions;  
  
  @Size(max = 50)
  private String amountAvailable;    
  
  @Size(max = 50)
  private String pretreatment;    

  private String alternativeContactInfo ;      

}
