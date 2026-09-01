package ca.gc.aafc.seqdb.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;
import ca.gc.aafc.seqdb.api.testsupport.factories.GenericMolecularAnalysisFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.SequenceControlledVocabularyItemFactory;
import jakarta.validation.ValidationException;

public class GenericMolecularAnalysisServiceIT extends SequenceModuleBaseIT {

  private static final String GROUP = "grp";

  @Test
  void assignedValueContainedInAcceptedValues_validationPasses() {
    SequenceControlledVocabularyItem testManagedAttribute = SequenceControlledVocabularyItemFactory.newSequenceControlledVocabularyItem()
      .acceptedValues(new String[]{"val1", "val2"})
      .dinaComponent(SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name())
      .controlledVocabulary(getManagedAttributeControlledVocabularyRef())
      .build();

    sequenceControlledVocabularyItemService.create(testManagedAttribute);

    GenericMolecularAnalysis genericMolecularAnalysis = GenericMolecularAnalysisFactory
      .newGenericMolecularAnalysis()
      .managedAttributes(Map.of(testManagedAttribute.getKey(), testManagedAttribute.getAcceptedValues()[0]))
      .build();

    assertDoesNotThrow(() -> genericMolecularAnalysisService.create(genericMolecularAnalysis));
  }

  @Test
  void validate_WhenInvalidIntegerType_ExceptionThrown() {
    SequenceControlledVocabularyItem testManagedAttribute =
       SequenceControlledVocabularyItemFactory.newSequenceControlledVocabularyItem()
        .createdBy("GenericMolecularAnalysisServiceIT")
        .dinaComponent(SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name())
        .group(GROUP)
        .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER)
        .acceptedValues(null)
        .controlledVocabulary(getManagedAttributeControlledVocabularyRef())
        .build();

    sequenceControlledVocabularyItemService.create(testManagedAttribute);

    GenericMolecularAnalysis genericMolecularAnalysis = GenericMolecularAnalysisFactory
      .newGenericMolecularAnalysis()
      .managedAttributes(Map.of(testManagedAttribute.getKey(), "1.2"))
      .build();

    assertThrows(ValidationException.class,
      () -> genericMolecularAnalysisService.create(genericMolecularAnalysis));
  }
}