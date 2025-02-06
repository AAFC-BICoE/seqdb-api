package ca.gc.aafc.seqdb.api.validation;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.dina.service.ManagedAttributeService;
import ca.gc.aafc.dina.validation.ManagedAttributeValueValidator;
import ca.gc.aafc.dina.validation.ValidationContext;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Named;
import lombok.NonNull;

@Component
public class SequenceManagedAttributeValueValidator extends ManagedAttributeValueValidator<SequenceManagedAttribute> {

  private static final String INVALID_VALIDATION_CONTEXT_KEY = "managedAttribute.validation.context.invalid";
  private static final String COMPONENT_FIELD_NAME = "managedAttributeComponent";

  private final ManagedAttributeService<SequenceManagedAttribute> dinaService;
  private final MessageSource messageSource;

  public SequenceManagedAttributeValueValidator(
    @Named("validationMessageSource") MessageSource baseMessageSource, // from dina-base
    @NonNull MessageSource messageSource,
    @NonNull ManagedAttributeService<SequenceManagedAttribute> dinaService) {
    super(baseMessageSource, dinaService);
    this.dinaService = dinaService;
    this.messageSource = messageSource;
  }

  public <D extends DinaEntity> void validate(D entity, Map<String, String> managedAttributes, SequenceManagedAttributeValidationContext context) {
    super.validate(entity, managedAttributes, context);
  }

  /**
   * override base class version to also add a restriction on the component since the uniqueness
   * for CollectionManagedAttribute is key + component.
   * @param keys
   * @param validationContext
   * @return
   */
  @Override
  protected Map<String, SequenceManagedAttribute> findAttributesForValidation(Set<String> keys, ValidationContext validationContext) {
    return dinaService.findAttributesForKeys(keys, Pair.of(COMPONENT_FIELD_NAME, validationContext.getValue()));
  }

  @Override
  protected boolean preValidateValue(SequenceManagedAttribute managedAttributeDefinition,
                                     String value, Errors errors, ValidationContext validationContext) {

    // expected context based on the component
    SequenceManagedAttributeValidationContext expectedContext =
      SequenceManagedAttributeValidationContext.from(managedAttributeDefinition.getManagedAttributeComponent());

    if (!expectedContext.equals(validationContext)) {
      errors.reject(INVALID_VALIDATION_CONTEXT_KEY, getMessageForKey(INVALID_VALIDATION_CONTEXT_KEY,
        Objects.toString(validationContext), expectedContext.toString()));
      return false;
    }
    return true;
  }

  /**
   * Wrapper class to expose {@link ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute.ManagedAttributeComponent} as
   * {@link ValidationContext}.
   */
  public static final class SequenceManagedAttributeValidationContext implements ValidationContext {
    // make sure to only keep 1 instance per enum value
    private static final EnumMap<SequenceManagedAttribute.ManagedAttributeComponent, SequenceManagedAttributeValidationContext>
      INSTANCES = new EnumMap<>(SequenceManagedAttribute.ManagedAttributeComponent.class);

    private final SequenceManagedAttribute.ManagedAttributeComponent managedAttributeComponent;

    /**
     * Use {@link #from(SequenceManagedAttribute.ManagedAttributeComponent)} method
     * @param managedAttributeComponent
     */
    private SequenceManagedAttributeValidationContext(SequenceManagedAttribute.ManagedAttributeComponent managedAttributeComponent) {
      this.managedAttributeComponent = managedAttributeComponent;
    }

    public static SequenceManagedAttributeValidationContext from(SequenceManagedAttribute.ManagedAttributeComponent managedAttributeComponent) {
      return INSTANCES.computeIfAbsent(managedAttributeComponent, SequenceManagedAttributeValidationContext::new);
    }

    @Override
    public String toString() {
      return managedAttributeComponent.toString();
    }

    @Override
    public Object getValue() {
      return managedAttributeComponent;
    }
  }
}
