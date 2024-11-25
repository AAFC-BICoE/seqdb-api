package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MultilingualTestFixture;

public class SequenceManagedAttributeFactory {

  public static SequenceManagedAttribute.SequenceManagedAttributeBuilder newManagedAttribute() {
    return SequenceManagedAttribute
      .builder()
      .uuid(UUID.randomUUID())
      .name(RandomStringUtils.randomAlphabetic(5))
      .group(RandomStringUtils.randomAlphabetic(5))
      .createdBy(RandomStringUtils.randomAlphabetic(5))
      .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.STRING)
      .acceptedValues(new String[]{"value"})
      .managedAttributeComponent(SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS)
      .multilingualDescription(MultilingualTestFixture.newMultilingualDescription());
  }

}
