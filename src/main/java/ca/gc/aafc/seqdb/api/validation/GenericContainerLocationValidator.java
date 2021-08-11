package ca.gc.aafc.seqdb.api.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.sanger.PcrBatchItem;
import ca.gc.aafc.seqdb.api.util.NumberLetterMappingUtils;
import lombok.NonNull;

@Component
public class GenericContainerLocationValidator implements Validator {

  private final MessageSource messageSource;

  public static final String VALID_NULL_WELL_COL = "validation.constraint.violation.nullWellCol";
  public static final String VALID_NULL_WELL_ROW = "validation.constraint.violation.nullWellRow";
  public static final String VALID_WELL_COL_CONTAINER_TYPE = "validation.constraint.violation.wellColContainerType";
  public static final String VALID_WELL_ROW_CONTAINER_TYPE = "validation.constraint.violation.wellRowContainerType";

  public GenericContainerLocationValidator(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return LibraryPrep.class.isAssignableFrom(clazz) || PcrBatchItem.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(@NonNull Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException("StorageUnitValidator not supported for class " + target.getClass());
    }

    if (target instanceof LibraryPrep) {
      LibraryPrep libraryPrep = (LibraryPrep) target;
      checkColAndRow(libraryPrep.getWellRow(), libraryPrep.getWellColumn(), errors);
      checkContainerType(libraryPrep.getWellRow(), libraryPrep.getWellColumn(), libraryPrep.getLibraryPrepBatch().getContainerType(), errors);
    }

    if (target instanceof PcrBatchItem) {
      PcrBatchItem pcrBatchItem = (PcrBatchItem) target;
      checkColAndRow(pcrBatchItem.getWellRow(), pcrBatchItem.getWellColumn(), errors);
      checkContainerType(pcrBatchItem.getWellRow(), pcrBatchItem.getWellColumn(), pcrBatchItem.getPcrBatch().getContainerType(), errors);
    }
  }

  private void checkColAndRow(String row, Integer col, Errors errors) {
    
    // Row and col must be either both set or both null.
    if (col == null && row != null) {
      String errorMessage = getMessage(VALID_NULL_WELL_COL);
      errors.rejectValue("wellColumn", VALID_NULL_WELL_COL, errorMessage);
    }
    if (row == null && col != null) {
      String errorMessage = getMessage(VALID_NULL_WELL_ROW);
      errors.rejectValue("wellRow", VALID_NULL_WELL_ROW, errorMessage);
    }
  }

  private void checkContainerType(String row, Integer col, @NonNull ContainerType cType, Errors errors) {

    // Validate well coordinates if they are set.
    if (row != null && col != null) {

      Integer rows = cType.getNumberOfRows();
      Integer cols = cType.getNumberOfColumns();
      
      if (col > cols) {
        String errorMessage = getMessage(VALID_WELL_COL_CONTAINER_TYPE, col).replace("''", "'");
        errors.rejectValue("wellColumn", VALID_WELL_COL_CONTAINER_TYPE, errorMessage);
      }
      
      if (NumberLetterMappingUtils.getNumber(row) > rows) {
        String errorMessage = getMessage(VALID_WELL_ROW_CONTAINER_TYPE, row).replace("''", "'");
        errors.rejectValue("wellRow", VALID_WELL_ROW_CONTAINER_TYPE, errorMessage);
      }
      
    }
  }

  private String getMessage(String key, Object... objects) {
    return messageSource.getMessage(key, objects, LocaleContextHolder.getLocale());
  }

}
