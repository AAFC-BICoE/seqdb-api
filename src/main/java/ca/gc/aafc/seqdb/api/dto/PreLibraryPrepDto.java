package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.repository.meta.JsonApiExternalRelation;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepType;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

import org.javers.core.metamodel.annotation.ShallowReference;

@Data
@JsonApiResource(type = "pre-library-prep")
@RelatedEntity(PreLibraryPrep.class)
public class PreLibraryPrepDto {

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;
  private String group;

  private PreLibraryPrepType preLibraryPrepType;

  private Double inputAmount;

  private Double targetBpSize;

  private Double averageFragmentSize;

  private Double concentration;

  private String quality;

  private String notes;

  @ShallowReference
  @JsonApiRelation
  private LibraryPrepDto libraryPrep;

  @JsonApiRelation
  private ProductDto product;

  @JsonApiExternalRelation(type = "protocol")
  @JsonApiRelation
  private ExternalRelationDto protocol;

}
