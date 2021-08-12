package ca.gc.aafc.seqdb.api.dto;

import java.util.List;

import ca.gc.aafc.dina.vocabulary.VocabularyConfiguration;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonApiResource(type = "vocabulary")
public class VocabularyDto {
  
  @JsonApiId
  private final String id;

  private final List<VocabularyConfiguration.VocabularyElement> vocabularyElements;

}
