package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "library-prep")
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

  @JsonApiExternalRelation(type = "storage-unit-usage")
  @JsonApiRelation
  private ExternalRelationDto storageUnitUsage;

  @JsonApiRelation
  private LibraryPrepBatchDto libraryPrepBatch;

  @JsonApiRelation
  private NgsIndexDto indexI5;

  @JsonApiRelation
  private NgsIndexDto indexI7;

  @JsonApiExternalRelation(type = "material-sample")
  @JsonApiRelation
  private ExternalRelationDto materialSample;


}
