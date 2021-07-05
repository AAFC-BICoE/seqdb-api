package ca.gc.aafc.seqdb.api.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = "library-prep-batch")
@RelatedEntity(LibraryPrepBatch.class)
public class LibraryPrepBatchDto {

  @JsonApiId
  private UUID uuid;

  private String name;

  private String createdBy;
  private OffsetDateTime createdOn;

  private Double totalLibraryYieldNm;

  private String notes;

  private String cleanUpNotes;

  private String yieldNotes;
  
  private LocalDate dateUsed;

  @JsonApiRelation
  private ProductDto product;

  @JsonApiRelation
  private ProtocolDto protocol;

  @JsonApiRelation
  private ContainerTypeDto containerType;

  @JsonApiRelation
  private ThermocyclerProfileDto thermocyclerProfile;

  @JsonApiRelation
  private IndexSetDto indexSet;

  @JsonApiRelation(opposite = "libraryPrepBatch")
  private List<LibraryPrepDto> libraryPreps;

}
