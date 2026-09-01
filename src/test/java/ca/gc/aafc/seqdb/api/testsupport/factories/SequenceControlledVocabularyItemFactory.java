package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MultilingualTestFixture;

public class SequenceControlledVocabularyItemFactory {

  public static SequenceControlledVocabularyItem.SequenceControlledVocabularyItemBuilder<?, ?> newSequenceControlledVocabularyItem() {
    return SequenceControlledVocabularyItem
      .builder()
      .uuid(UUID.randomUUID())
      .name(RandomStringUtils.randomAlphabetic(5))
      .group(RandomStringUtils.randomAlphabetic(5))
      .createdBy(RandomStringUtils.randomAlphabetic(5))
      .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.STRING)
      .acceptedValues(new String[]{"value"})
      .dinaComponent(SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name())
      .multilingualDescription(MultilingualTestFixture.newMultilingualDescription());
  }
}
