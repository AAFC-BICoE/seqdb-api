package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "thermocyclerprofile")
@SuppressFBWarnings(value="EI_EXPOSE_REP")
public class ThermocyclerProfileDto {

  @JsonApiId
  private Integer pcrProfileId;

  private String name;
  
  private String application;
  
  private String cycles;
  
  private Timestamp lastModified;

  private String step1;

  private String step2;

  private String step3;

  private String step4;

  private String step5;

  private String step6;

  private String step7;

  private String step8;

  private String step9;

  private String step10;

  private String step11;

  private String step12;

  private String step13;

  private String step14;

  private String step15;

  @JsonApiRelation
  private RegionDto region;

  @JsonApiRelation
  private GroupDto group;

}
