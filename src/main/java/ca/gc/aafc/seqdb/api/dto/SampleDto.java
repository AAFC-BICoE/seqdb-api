package ca.gc.aafc.seqdb.api.dto;

import java.sql.Date;
import java.sql.Timestamp;

import ca.gc.aafc.seqdb.entities.Sample.SampleType;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "sample")
public class SampleDto {

  @JsonApiId
  private Integer sampleId;
  
  private SampleType sampleType;
  
  private String name;
  
  private String experimenter;
  
  private String version;
  
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

  private Double ratio260_280;

  private Double ratio260_230;

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
  
  private Boolean discarded;
  
  private Date dateDiscarded;

  private Timestamp lastModified;
  
  @JsonApiRelation
  private GroupDto group;
  
  @JsonApiRelation
  private ProductDto kit;
  
  @JsonApiRelation
  private ProtocolDto protocol;
  
}
