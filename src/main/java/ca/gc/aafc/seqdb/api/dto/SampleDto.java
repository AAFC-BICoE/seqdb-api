package ca.gc.aafc.seqdb.api.dto;

import java.sql.Date;
import java.sql.Timestamp;

import ca.gc.aafc.seqdb.entities.Sample.SampleType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "sample")
@SuppressFBWarnings(value="EI_EXPOSE_REP")
public class SampleDto {

  @JsonApiId
  private Integer sampleId;
  
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

  //CHECKSTYLE:OFF MemberName
  private Double ratio260_280;

  private Double ratio260_230;
  //CHECKSTYLE:ON MemberName

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

  private Timestamp lastModified;
  
  // Optional Relations
  
  @JsonApiRelation
  private GroupDto group;
  
  @JsonApiRelation
  private ProductDto kit;
  
  @JsonApiRelation
  private ProtocolDto protocol;
  
}
