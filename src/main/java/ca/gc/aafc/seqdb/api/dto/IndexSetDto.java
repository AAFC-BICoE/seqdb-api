package ca.gc.aafc.seqdb.api.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiRelation;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data
@JsonApiResource(type = IndexSetDto.TYPENAME)
@RelatedEntity(IndexSet.class)
public class IndexSetDto {

  public static final String TYPENAME = "index-set";

  @JsonApiId
  private UUID uuid;

  private String createdBy;
  private OffsetDateTime createdOn;

  private String group;

  private String name;

  private String forwardAdapter;

  private String reverseAdapter;

  @JsonApiRelation(opposite = "indexSet")
  private List<NgsIndexDto> ngsIndexes;

}
