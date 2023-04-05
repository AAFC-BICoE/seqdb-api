package ca.gc.aafc.seqdb.api.validation;

import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PcrBatchValidator implements Validator {

  static final String IS_COMPLETE_ERROR_MESSAGE_KEY = "validation.constraint.violation.pcrbatch.isComplete";

  private final MessageSource messageSource;

  public PcrBatchValidator(MessageSource messageSource) {
    this.messageSource = messageSource;
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
    checkNotComplete(errors, (PcrBatch) target);
    checkStorageOrTypeNotBoth(errors, (PcrBatch) target);
  }

  /**
   * Ensures a PCRBatch is not set as complete.
   * @param errors
   * @param pcrBatch
   */
  private void checkNotComplete(Errors errors, PcrBatch pcrBatch) {
    if (pcrBatch.getIsComplete()) {
      String errorMessage = getMessage(IS_COMPLETE_ERROR_MESSAGE_KEY);
      errors.rejectValue("isComplete", IS_COMPLETE_ERROR_MESSAGE_KEY, errorMessage);
    }
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
