package ca.gc.aafc.seqdb.api.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;

@Component
public class PcrBatchValidator implements Validator {

  private final PcrBatchVocabularyValidator pcrBatchVocabularyValidator;

  public PcrBatchValidator(PcrBatchVocabularyValidator pcrBatchVocabularyValidator) {
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
    pcrBatchVocabularyValidator.validate(target, errors);
  }
}
