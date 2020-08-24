package ca.gc.aafc.seqdb.api.dto;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "containerType")
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
@RelatedEntity(ContainerType.class)
public class ContainerTypeDto {

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String name;

  private Integer numberOfColumns;

  private Integer numberOfRows;

  private Timestamp lastModified;

}
