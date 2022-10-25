package ca.gc.aafc.seqdb.api.validation;

import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PcrBatchValidator implements Validator {

  private static final String ERROR_MESSAGE_KEY = "validation.constraint.violation.storageUnitOrType";

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
    checkStorageOrTypeNotBoth(errors, (PcrBatch) target);
  }

  private void checkStorageOrTypeNotBoth(Errors errors, PcrBatch pcrBatch) {
    if (pcrBatch.getStorageUnit() != null && pcrBatch.getStorageUnitType() != null) {
      String errorMessage = getMessage(ERROR_MESSAGE_KEY);
      errors.rejectValue("storageUnit", ERROR_MESSAGE_KEY, errorMessage);
    }
  }

  private String getMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }
}
