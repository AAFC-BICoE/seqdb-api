package ca.gc.aafc.seqdb.api.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.ControlledVocabularyItemService;
import ca.gc.aafc.dina.validation.ControlledVocabularyItemValidator;
import ca.gc.aafc.dina.validation.ManagedAttributeValueValidatorV2;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;

/**
 * This class is responsible to check if a vocabulary item is used before deletion.
 * Deletion is still a risky operation since it is impossible to be 100% sure it is not used.
 */
@Service
public class SequenceControlledVocabularyItemService extends ControlledVocabularyItemService<SequenceControlledVocabularyItem> {

  // we are using ObjectProvider since it is a circular dependency, so it will be lazy initialized
  private final ObjectProvider<ManagedAttributeValueValidatorV2<SequenceControlledVocabularyItem>> valueValidators;

  public SequenceControlledVocabularyItemService(BaseDAO baseDAO, SmartValidator smartValidator,
                                                    ControlledVocabularyItemValidator itemValidator,
                                                    ObjectProvider<ManagedAttributeValueValidatorV2<SequenceControlledVocabularyItem>> valueValidators) {
    super(baseDAO, smartValidator, SequenceControlledVocabularyItem.class, itemValidator);
    this.valueValidators = valueValidators;
  }

  @Override
  protected void preDelete(SequenceControlledVocabularyItem entity) {

    for (ManagedAttributeValueValidatorV2<SequenceControlledVocabularyItem> valueValidator : valueValidators.stream()
      .toList()) {
      if (valueValidator.isApplicableTo(entity)) {
        if (!valueValidator.canBeDeleted(entity)) {
          throw new IllegalStateException(
            "Managed attribute key: " + entity.getKey() + ", is currently in use.");
        }
      }
    }
  }
}
