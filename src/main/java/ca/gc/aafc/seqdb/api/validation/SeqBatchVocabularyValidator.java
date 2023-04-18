package ca.gc.aafc.seqdb.api.validation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.vocabulary.VocabularyElementConfiguration;
import ca.gc.aafc.seqdb.api.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;

/**
 * Participates in SeqBatchValidator
 */
@Component
class SeqBatchVocabularyValidator extends VocabularyBasedValidator<SeqBatch> {

  private static final String BATCH_TYPE_FIELD_NAME = "sequencingType";
  private final List<VocabularyElementConfiguration> sequencingTypeVocabulary;

  SeqBatchVocabularyValidator(MessageSource messageSource, SequenceVocabularyConfiguration vocabularyConfiguration) {
    super(messageSource, SeqBatch.class);
    sequencingTypeVocabulary = vocabularyConfiguration.getVocabularyByKey(SequenceVocabularyConfiguration.SEQUENCING_TYPE_VOCAB_KEY);
  }

  @Override
  protected void validateVocabularyBasedAttribute(SeqBatch target, Errors errors) {
    if (StringUtils.isBlank(target.getSequencingType())) {
      return;
    }

    target.setSequencingType(validateAndStandardizeValueAgainstVocabulary(target.getSequencingType(),
      BATCH_TYPE_FIELD_NAME, sequencingTypeVocabulary, errors));
  }
}
