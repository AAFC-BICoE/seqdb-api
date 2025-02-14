package ca.gc.aafc.seqdb.api.validation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.vocabulary.VocabularyElementConfiguration;
import ca.gc.aafc.seqdb.api.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;

/**
 * Participates in PcrBatchValidator
 */
@Component
public class PcrBatchVocabularyValidator extends VocabularyBasedValidator<PcrBatch> {

  private static final String BATCH_TYPE_FIELD_NAME = "batchType";
  private final List<VocabularyElementConfiguration> batchTypeVocabulary;

  PcrBatchVocabularyValidator(MessageSource messageSource, SequenceVocabularyConfiguration vocabularyConfiguration) {
    super(PcrBatch.class, messageSource);
    batchTypeVocabulary = vocabularyConfiguration.getVocabularyByKey(SequenceVocabularyConfiguration.PCR_BATCH_TYPE_VOCAB_KEY);
  }

  @Override
  public void validateTarget(PcrBatch target, Errors errors) {
    if (StringUtils.isBlank(target.getBatchType())) {
      return;
    }

    target.setBatchType(validateAndStandardizeValueAgainstVocabulary(target.getBatchType(),
      BATCH_TYPE_FIELD_NAME, batchTypeVocabulary, errors));
  }
}
