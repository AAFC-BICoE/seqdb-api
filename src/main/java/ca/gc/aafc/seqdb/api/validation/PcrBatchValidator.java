package ca.gc.aafc.seqdb.api.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;

@Component
public class PcrBatchValidator implements Validator {

  private final MessageSource messageSource;
  private final PcrBatchVocabularyValidator pcrBatchVocabularyValidator;

  public PcrBatchValidator(MessageSource messageSource, PcrBatchVocabularyValidator pcrBatchVocabularyValidator) {
    this.messageSource = messageSource;
    this.pcrBatchVocabularyValidator = pcrBatchVocabularyValidator;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return PcrBatch.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException("PcrBatchValidator not supported for class " + target.getClass());
    }
    checkStorageOrTypeNotBoth(errors, (PcrBatch) target);

    pcrBatchVocabularyValidator.validate(target, errors);
  }

  private void checkStorageOrTypeNotBoth(Errors errors, PcrBatch pcrBatch) {
    if (pcrBatch.getStorageUnit() != null && pcrBatch.getStorageUnitType() != null) {
      String errorMessage = getMessage(SharedMessageKey.STORAGE_TYPE_OR_UNIT_ERROR_MESSAGE_KEY);
      errors.rejectValue("storageUnit", SharedMessageKey.STORAGE_TYPE_OR_UNIT_ERROR_MESSAGE_KEY, errorMessage);
    }
  }

  private String getMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }

}
