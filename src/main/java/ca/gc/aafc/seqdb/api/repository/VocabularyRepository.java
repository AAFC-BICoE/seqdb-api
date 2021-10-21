package ca.gc.aafc.seqdb.api.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.seqdb.api.SeqdbVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.dto.VocabularyDto;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ReadOnlyResourceRepositoryBase;
import io.crnk.core.resource.list.ResourceList;
import lombok.NonNull;

@Repository
public class VocabularyRepository extends ReadOnlyResourceRepositoryBase<VocabularyDto, String> {
  
  private final List<VocabularyDto> vocabulary;

  protected VocabularyRepository(
    @NonNull SeqdbVocabularyConfiguration vocabularyConfiguration) {
    super(VocabularyDto.class);

    vocabulary = vocabularyConfiguration.getVocabulary()
        .entrySet()
        .stream()
        .map( entry -> new VocabularyDto(entry.getKey(), entry.getValue()))
        .collect( Collectors.toList());
  }

  @Override
  public ResourceList<VocabularyDto> findAll(QuerySpec querySpec) {
    return querySpec.apply(vocabulary);
  }
}
