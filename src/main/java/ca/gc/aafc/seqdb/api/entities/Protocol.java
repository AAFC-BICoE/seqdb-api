package ca.gc.aafc.seqdb.api.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Protocols", uniqueConstraints = {
@UniqueConstraint(columnNames = { "Name", "Type" }) })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressFBWarnings({ "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class Protocol {

  @AllArgsConstructor
  public enum ProtocolType {
    COLLECTION_EVENT("Collection Event"),
    SPECIMEN_PREPARATION("Specimen Preparation"),
    DNA_EXTRACTION("DNA Extraction"),
    PCR_REACTION("PCR Reaction"),
    SEQ_REACTION("Sequencing Reaction");

    @Getter
    private final String value;
  }

  @Getter(onMethod=@__({
    @Id,
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  }))
  private Integer id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ProtocolType type;

  @NotBlank
  @Size(max = 50)
  private String name;

  @Size(max = 5)
  private String version;
  private String description;
  private String steps;
  private String notes;
  private String reference;

  @Size(max = 50)
  private String equipment;

  @Size(max = 50)
  private String forwardPrimerConcentration;

  @Size(max = 50)
  private String reversePrimerConcentration;

  @Size(max = 50)
  private String reactionMixVolume;

  @Size(max = 50)
  private String reactionMixVolumePerTube;

  @Getter(onMethod=@__({
    @ManyToOne(fetch = FetchType.LAZY),
    @JoinColumn(name = "product_id")
  }))
  private Product kit;

  private Timestamp lastModified;

}