package ca.gc.aafc.seqdb.api.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.Sample;
import ca.gc.aafc.seqdb.api.entities.Sample.SampleType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "sample")
@SuppressFBWarnings(value="EI_EXPOSE_REP")
@RelatedEntity(Sample.class)
public class SampleDto {

  @JsonApiId
  private Integer id;
  
  // Required Fields
  
  private String name;
  
  private String version;  
  
  // Optional Fields
  
  private SampleType sampleType;
  
  private String experimenter;

  private String versionDescription;
  
  private String treatment;
  
  private String source;
  
  private String dnaConcentration;
  
  private String dnaConcentrationNotes;
  
  private String dnaConcentrationPerStartMaterial;
  
  private Date date;

  private String nuclAcidExt;

  private String extractionBatch;

  private Double pelletSize;
 
  private Double lysisBufferVolume;

  private Double proteinaseKVolume;

  private Double qubitDNAConcentration;

  private String quantificationMethod;

  private String growthMedia;

  private String dnaNotes;

  private String notes;

  private String tubeId;

  private Boolean unusableDna;
  
  private Date inoculationDate;
  
  private String fraction;
  
  private Double fermentationTemperature;
  
  private String fermentationTime;
  
  private String extractionSolvent;
  
  private LocalDate dateDiscarded;
  
  private String discardedNotes;

  private Timestamp lastModified;
  
  // Optional Relations
  
  @JsonApiRelation
  private ProductDto kit;
  
  @JsonApiRelation
  private ProtocolDto protocol;
  
}
