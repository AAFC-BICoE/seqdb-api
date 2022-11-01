package ca.gc.aafc.seqdb.api.validation;

import ca.gc.aafc.dina.entity.StorageGridLayout;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ca.gc.aafc.seqdb.api.entities.ContainerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Component
public class ContainerLocationValidator extends AbstractLocationValidator {

  public ContainerLocationValidator(MessageSource messageSource) {
    super(messageSource);
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return ContainerLocationArgs.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(@NonNull Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException("ContainerLocationValidator not supported for class " + target.getClass());
    }

    ContainerLocationArgs containerLocationArgs = (ContainerLocationArgs) target;

    checkRowAndColumn(containerLocationArgs.getWellRow(), containerLocationArgs.getWellColumn(), errors);
    checkWellAgainstGrid(containerLocationArgs.getWellRow(), containerLocationArgs.getWellColumn(),
            containerLocationArgs.getStorageGridLayout(), errors);
  }

  @Getter
  @AllArgsConstructor(staticName = "of")
  public static class ContainerLocationArgs {

    private String wellRow;
    private Integer wellColumn;
    private ContainerType containerType;

    public StorageGridLayout getStorageGridLayout() {
      if(containerType == null) {
        return null;
      }
      return StorageGridLayout.builder()
              .numberOfRows(containerType.getNumberOfRows())
              .numberOfColumns(containerType.getNumberOfColumns())
              .fillDirection(StorageGridLayout.FillDirection.BY_ROW)
              .build();
    }
  }

}
