package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.dto.SequenceControlledVocabularyItemDto;

public class SequenceControlledVocabularyItemTestFixture {

  public static final String GROUP = "dina";

  public static SequenceControlledVocabularyItemDto newSequenceControlledVocabularyItemDto() {
    SequenceControlledVocabularyItemDto collectionManagedAttributeDto = new SequenceControlledVocabularyItemDto();
    collectionManagedAttributeDto.setName(RandomStringUtils.randomAlphabetic(5));
    collectionManagedAttributeDto.setGroup(GROUP);
    collectionManagedAttributeDto.setVocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER);
    collectionManagedAttributeDto.setAcceptedValues(new String[]{"1", "2"});
    collectionManagedAttributeDto.setUnit("cm");
    collectionManagedAttributeDto.setDinaComponent(SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name());
    collectionManagedAttributeDto.setCreatedBy("created by");
    collectionManagedAttributeDto.setMultilingualDescription(MultilingualTestFixture.newMultilingualDescription());
    return collectionManagedAttributeDto;
  }
}
