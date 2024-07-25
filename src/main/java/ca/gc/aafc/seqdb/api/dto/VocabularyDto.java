package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.vocabulary.VocabularyElementConfiguration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

@AllArgsConstructor
@Getter
@JsonApiTypeForClass(VocabularyDto.TYPE)
public class VocabularyDto {

  public static final String TYPE = "vocabulary";

  @JsonApiId
  private final String id;

  private final List<VocabularyElementConfiguration> vocabularyElements;
}
