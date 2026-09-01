package ca.gc.aafc.seqdb.api.validation;

import static ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration.MANAGED_ATTRIBUTE_VOCAB_UUID;

import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import ca.gc.aafc.dina.service.ControlledVocabularyItemService;
import ca.gc.aafc.dina.service.PostgresJsonbService;
import ca.gc.aafc.dina.validation.ManagedAttributeValueValidatorV2;
import ca.gc.aafc.seqdb.api.config.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;
import jakarta.inject.Named;
import lombok.NonNull;

/**
 * For GENERIC_MOLECULAR_ANALYSIS managed attribute
 */
@Component
public class SequenceManagedAttributeValueValidatorGenericMolecularAnalysis extends ManagedAttributeValueValidatorV2<SequenceControlledVocabularyItem> {
  public static final String GENERIC_MOLECULAR_ANALYSIS_TABLE_NAME = "generic_molecular_analysis";
  public static final String MANAGED_ATTRIBUTES_COL_NAME = "managed_attributes";

  private final PostgresJsonbService jsonbService;

  public SequenceManagedAttributeValueValidatorGenericMolecularAnalysis(@Named("validationMessageSource")MessageSource messageSource,
                                                                @NonNull ControlledVocabularyItemService<SequenceControlledVocabularyItem> vocabItemService,
                                                                PostgresJsonbService jsonbService) {
    super(messageSource, vocabItemService);
    this.jsonbService = jsonbService;
  }

  @Override
  public UUID getControlledVocabularyUuid() {
    return MANAGED_ATTRIBUTE_VOCAB_UUID;
  }

  @Override
  public String getDinaComponent() {
    return SequenceVocabularyConfiguration.DinaComponent.GENERIC_MOLECULAR_ANALYSIS.name();
  }

  @Override
  public boolean canBeDeleted(SequenceControlledVocabularyItem controlledVocabularyItem) {
    return jsonbService.countFirstLevelKeys(
      GENERIC_MOLECULAR_ANALYSIS_TABLE_NAME, MANAGED_ATTRIBUTES_COL_NAME, controlledVocabularyItem.getKey()) ==
      0;
  }
}
