package ca.gc.aafc.seqdb.api.validation;

import jakarta.inject.Named;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import ca.gc.aafc.dina.validation.ControlledVocabularyItemValidator;

@Component
public class SequenceControlledVocabularyItemValidator extends ControlledVocabularyItemValidator {
  public SequenceControlledVocabularyItemValidator(@Named("validationMessageSource")
                                                     MessageSource messageSource) {
    super(messageSource);
  }
}
