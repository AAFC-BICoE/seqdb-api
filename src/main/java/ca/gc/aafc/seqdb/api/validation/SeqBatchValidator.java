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
  private final SeqBatchVocabularyValidator seqBatchVocabularyValidator;

  public SeqBatchValidator(MessageSource messageSource, SeqBatchVocabularyValidator seqBatchVocabularyValidator) {
    this.messageSource = messageSource;
    this.seqBatchVocabularyValidator = seqBatchVocabularyValidator;
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
    seqBatchVocabularyValidator.validate(target, errors);
  }

  private String getMessage(String key) {
    return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
  }
}
