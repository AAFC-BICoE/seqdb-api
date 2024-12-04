package ca.gc.aafc.seqdb.api.service;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;
import ca.gc.aafc.seqdb.api.testsupport.factories.GenericMolecularAnalysisFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.SequenceManagedAttributeFactory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import javax.validation.ValidationException;

public class GenericMolecularAnalysisServiceIT extends SequenceModuleBaseIT {

  private static final String GROUP = "grp";

  @Test
  void assignedValueContainedInAcceptedValues_validationPasses() {
    SequenceManagedAttribute testManagedAttribute = SequenceManagedAttributeFactory.newManagedAttribute()
      .acceptedValues(new String[]{"val1", "val2"})
      .managedAttributeComponent(SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS)
      .build();

    managedAttributeService.create(testManagedAttribute);

    GenericMolecularAnalysis genericMolecularAnalysis = GenericMolecularAnalysisFactory
      .newGenericMolecularAnalysis()
      .managedAttributes(Map.of(testManagedAttribute.getKey(), testManagedAttribute.getAcceptedValues()[0]))
      .build();

    assertDoesNotThrow(() -> genericMolecularAnalysisService.create(genericMolecularAnalysis));
  }

  @Test
  void validate_WhenInvalidIntegerType_ExceptionThrown() {
    SequenceManagedAttribute testManagedAttribute =
      SequenceManagedAttributeFactory.newManagedAttribute()
        .createdBy("GenericMolecularAnalysisServiceIT")
        .managedAttributeComponent(
          SequenceManagedAttribute.ManagedAttributeComponent.GENERIC_MOLECULAR_ANALYSIS)
        .group(GROUP)
        .vocabularyElementType(TypedVocabularyElement.VocabularyElementType.INTEGER)
        .acceptedValues(null)
        .build();

    managedAttributeService.create(testManagedAttribute);

    GenericMolecularAnalysis genericMolecularAnalysis = GenericMolecularAnalysisFactory
      .newGenericMolecularAnalysis()
      .managedAttributes(Map.of(testManagedAttribute.getKey(), "1.2"))
      .build();

    assertThrows(ValidationException.class,
      () -> genericMolecularAnalysisService.create(genericMolecularAnalysis));
  }
}