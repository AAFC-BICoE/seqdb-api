package ca.gc.aafc.seqdb.api.validation;

import javax.inject.Named;

import ca.gc.aafc.dina.validation.AbstractStorageLocationValidator;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PcrBatchItemValidator extends AbstractStorageLocationValidator {

  public PcrBatchItemValidator(@Named("validationMessageSource") MessageSource messageSource) {
    super(messageSource);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return PcrBatchItem.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException("PcrBatchItemValidator not supported for class " + target.getClass());
    }
    PcrBatchItem pcrBatchItem = (PcrBatchItem) target;
    // make sure we have a location to validate
    if (StringUtils.isBlank(pcrBatchItem.getWellRow()) && pcrBatchItem.getWellColumn() == null) {
      return;
    }
    checkRowAndColumn(pcrBatchItem.getWellRow(), pcrBatchItem.getWellColumn(), errors);

    // if there is no storage restriction defined, we can't validate.
    // we return and allow the location since we can't say for sure it's invalid
    if(pcrBatchItem.getPcrBatch() == null || pcrBatchItem.getPcrBatch().getStorageRestriction() == null) {
      return;
    }

    checkWellAgainstGrid(pcrBatchItem.getWellRow(), pcrBatchItem.getWellColumn(),
            pcrBatchItem.getPcrBatch().getStorageRestriction().getLayout(), errors);
  }

}
