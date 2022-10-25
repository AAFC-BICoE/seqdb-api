package ca.gc.aafc.seqdb.api.validation;

import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PcrBatchItemValidator extends AbstractLocationValidator {

  public PcrBatchItemValidator(MessageSource messageSource) {
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

    checkRowAndColumn(pcrBatchItem.getWellRow(), pcrBatchItem.getWellColumn(), errors);
    //todo check for null getStorageRestriction
    checkWellAgainstGrid(pcrBatchItem.getWellRow(), pcrBatchItem.getWellColumn(),
            pcrBatchItem.getPcrBatch().getStorageRestriction().getLayout(), errors);
  }


}
