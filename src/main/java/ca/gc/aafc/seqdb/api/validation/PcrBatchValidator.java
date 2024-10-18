package ca.gc.aafc.seqdb.api.validation;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.validation.DinaBaseValidator;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;

@Component
public class PcrBatchValidator extends DinaBaseValidator<PcrBatch> {

  private final PcrBatchVocabularyValidator pcrBatchVocabularyValidator;

  public PcrBatchValidator(MessageSource messageSource, PcrBatchVocabularyValidator pcrBatchVocabularyValidator) {
    super(PcrBatch.class, messageSource);
    this.pcrBatchVocabularyValidator = pcrBatchVocabularyValidator;
  }

  @Override
  public void validateTarget(PcrBatch target, Errors errors) {
    pcrBatchVocabularyValidator.validate(target, errors);
  }
}
