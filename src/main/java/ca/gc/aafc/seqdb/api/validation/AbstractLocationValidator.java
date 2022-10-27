package ca.gc.aafc.seqdb.api.validation;

import ca.gc.aafc.dina.entity.StorageGridLayout;
import ca.gc.aafc.dina.translator.NumberLetterTranslator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * package protected abstract location validator.
 */
abstract class AbstractLocationValidator implements Validator {

  public static final String VALID_NULL_WELL_COL = "validation.constraint.violation.nullWellCol";
  public static final String VALID_NULL_WELL_ROW = "validation.constraint.violation.nullWellRow";
  public static final String INVALID_WELL_LOCATION = "validation.constraint.violation.invalidWellLocation";

  private final MessageSource messageSource;

  AbstractLocationValidator(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Validate that column and row are provided together, as a pair.
   * @param row
   * @param col
   * @param errors
   */
  protected void checkRowAndColumn(String row, Integer col, Errors errors) {
    // Row and col must be either both set or both null.
    if (col == null && StringUtils.isNotBlank(row)) {
      String errorMessage = getMessage(VALID_NULL_WELL_COL);
      errors.rejectValue("wellColumn", VALID_NULL_WELL_COL, errorMessage);
    }
    if (StringUtils.isBlank(row) && col != null) {
      String errorMessage = getMessage(VALID_NULL_WELL_ROW);
      errors.rejectValue("wellRow", VALID_NULL_WELL_ROW, errorMessage);
    }
  }

  protected void checkWellAgainstGrid(String row, Integer col, StorageGridLayout sgl, Errors errors) {
    // Validate well coordinates if they are set.
    if (row != null && col != null) {
      if (!sgl.isValidLocation(NumberLetterTranslator.toNumber(row), col)) {
        String errorMessage = getMessage(INVALID_WELL_LOCATION, row + col);
        // we need to attach the error to a specific field but here it could be col OR row
        errors.rejectValue("wellColumn", INVALID_WELL_LOCATION, errorMessage);
      }
    }
  }

  private String getMessage(String key, Object... objects) {
    return messageSource.getMessage(key, objects, LocaleContextHolder.getLocale());
  }
}
