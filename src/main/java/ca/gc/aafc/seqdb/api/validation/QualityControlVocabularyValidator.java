package ca.gc.aafc.seqdb.api.validation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.vocabulary.VocabularyElementConfiguration;
import ca.gc.aafc.seqdb.api.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.entities.QualityControl;

@Component
public class QualityControlVocabularyValidator extends VocabularyBasedValidator<QualityControl> {

  private static final String QUALITY_CONTROL_FIELD_NAME = "qcType";
  private final List<VocabularyElementConfiguration> qcTypeVocabulary;

  QualityControlVocabularyValidator(MessageSource messageSource, SequenceVocabularyConfiguration vocabularyConfiguration) {
    super(QualityControl.class, messageSource);
    qcTypeVocabulary = vocabularyConfiguration.getVocabularyByKey(SequenceVocabularyConfiguration.QUALITY_CONTROL_TYPE_VOCAB_KEY);
  }

  @Override
  public void validateTarget(QualityControl target, Errors errors) {
    if (StringUtils.isBlank(target.getQcType())) {
      return;
    }

    target.setQcType(validateAndStandardizeValueAgainstVocabulary(target.getQcType(),
      QUALITY_CONTROL_FIELD_NAME, qcTypeVocabulary, errors));
  }
}
