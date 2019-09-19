package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "containerType")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class ContainerTypeDto {

  @JsonApiId
  private Integer containerTypeId;

  private String name;

  private String baseType;

  private Integer numberOfColumns;

  private Integer numberOfRows;

  private Integer numberOfWells;

  private Timestamp lastModified;

  @JsonApiRelation
  private GroupDto group;

}
