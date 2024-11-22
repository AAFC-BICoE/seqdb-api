package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import org.apache.commons.lang3.RandomStringUtils;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.dto.SequenceManagedAttributeDto;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;

public class SequenceManagedAttributeTestFixture {

  public static final String GROUP = "dina";

  public static SequenceManagedAttributeDto newManagedAttribute() {
    SequenceManagedAttributeDto collectionManagedAttributeDto = new SequenceManagedAttributeDto();
    collectionManagedAttributeDto.setName(RandomStringUtils.randomAlphabetic(5));
    collectionManagedAttributeDto.setGroup(GROUP);
    collectionManagedAttributeDto.setVocabularyElementType(
      TypedVocabularyElement.VocabularyElementType.INTEGER);
    collectionManagedAttributeDto.setAcceptedValues(new String[]{"1", "2"});
    collectionManagedAttributeDto.setUnit("cm");
    collectionManagedAttributeDto.setManagedAttributeComponent(SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS);
    collectionManagedAttributeDto.setCreatedBy("created by");
    collectionManagedAttributeDto.setMultilingualDescription(MultilingualTestFixture.newMultilingualDescription());
    return collectionManagedAttributeDto;
  }


}
