package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "libraryPrep")
@RelatedEntity(LibraryPrep.class)
public class LibraryPrepDto {
  
  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;

  private Double inputNg;

  private String quality;

  private String size;

  private Integer wellColumn;

  private String wellRow;

  @JsonApiRelation
  private LibraryPrepBatchDto libraryPrepBatch;
  
  @JsonApiRelation
  private SampleDto sample;

  @JsonApiRelation
  private NgsIndexDto indexI5;

  @JsonApiRelation
  private NgsIndexDto indexI7;

}
