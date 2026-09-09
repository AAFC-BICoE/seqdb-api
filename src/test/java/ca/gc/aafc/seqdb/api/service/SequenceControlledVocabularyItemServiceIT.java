package ca.gc.aafc.seqdb.api.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration.DinaComponent;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;
import ca.gc.aafc.seqdb.api.testsupport.factories.SequenceControlledVocabularyItemFactory;

public class SequenceControlledVocabularyItemServiceIT extends SequenceModuleBaseIT {

  private static final String GROUP = "grp";

  @Test
  void delete_WhenNotInUse_DeleteAccepted() {
    SequenceControlledVocabularyItem attribute = newAttribute(SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS);
    attribute.setControlledVocabulary(getManagedAttributeControlledVocabularyRef());
    sequenceControlledVocabularyItemService.create(attribute);

    assertNotNull(
      sequenceControlledVocabularyItemService.findOne(attribute.getUuid(), SequenceControlledVocabularyItem.class));

    // To enable when usage is implemented
    sequenceControlledVocabularyItemService.delete(attribute);

    assertNull(
      sequenceControlledVocabularyItemService.findOne(attribute.getUuid(), SequenceControlledVocabularyItem.class));
  }

  private static SequenceControlledVocabularyItem newAttribute(DinaComponent dinaComponent) {
    return SequenceControlledVocabularyItemFactory.newSequenceControlledVocabularyItem()
      .createdBy("SequenceManagedAttributeServiceIT")
      .dinaComponent(dinaComponent.name())
      .group(GROUP)
      .acceptedValues(null)
      .build();
  }
}
