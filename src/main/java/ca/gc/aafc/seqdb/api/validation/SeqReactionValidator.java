package ca.gc.aafc.seqdb.api.validation;

import javax.inject.Named;

import ca.gc.aafc.dina.validation.AbstractStorageLocationValidator;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class SeqReactionValidator extends AbstractStorageLocationValidator {

  public SeqReactionValidator(@Named("validationMessageSource") MessageSource messageSource) {
    super(messageSource);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return SeqReaction.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException("SeqReactionValidator not supported for class " + target.getClass());
    }
    SeqReaction seqReaction = (SeqReaction) target;
    // make sure we have a location to validate
    if (StringUtils.isBlank(seqReaction.getWellRow()) && seqReaction.getWellColumn() == null) {
      return;
    }
    checkRowAndColumn(seqReaction.getWellRow(), seqReaction.getWellColumn(), errors);
  }

}
