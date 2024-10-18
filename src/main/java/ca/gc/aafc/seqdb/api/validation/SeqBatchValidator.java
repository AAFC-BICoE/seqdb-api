package ca.gc.aafc.seqdb.api.validation;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.validation.DinaBaseValidator;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;

@Component
public class SeqBatchValidator extends DinaBaseValidator<SeqBatch> {

  private final SeqBatchVocabularyValidator seqBatchVocabularyValidator;

  public SeqBatchValidator(MessageSource messageSource, SeqBatchVocabularyValidator seqBatchVocabularyValidator) {
    super(SeqBatch.class, messageSource);
    this.seqBatchVocabularyValidator = seqBatchVocabularyValidator;
  }

  @Override
  public void validateTarget(SeqBatch target, Errors errors) {
    seqBatchVocabularyValidator.validate(target, errors);
  }

}
