package ca.gc.aafc.seqdb.api.validation;

import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SeqBatchValidator implements Validator {

  private final MessageSource messageSource;

  public SeqBatchValidator(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return SeqBatch.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException("SeqBatchValidator not supported for class " + target.getClass());
    }
    checkStorageOrTypeNotBoth(errors, (SeqBatch) target);
  }

  private void checkStorageOrTypeNotBoth(Errors errors, SeqBatch seqBatch) {
    if (seqBatch.getStorageUnit() != null && seqBatch.getStorageUnitType() != null) {
      String errorMessage = getMessage(SharedMessageKey.STORAGE_TYPE_OR_UNIT_ERROR_MESSAGE_KEY);
      errors.rejectValue("storageUnit", SharedMessageKey.STORAGE_TYPE_OR_UNIT_ERROR_MESSAGE_KEY, errorMessage);
    }
  }

  private String getMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }
}
