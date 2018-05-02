package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.QuerySpec;
import lombok.NonNull;

/**
 * Provides methods for handling sparse field sets.
 */
public class FieldsHandler {
  
  private final EntityManager entityManager;
  
  public FieldsHandler(
      @NonNull EntityManager entityManager
  ) {
    this.entityManager = entityManager;
  }
  
  /**
   * Gets the selected fields as strings from the querySpec.
   * 
   * @param querySpec
   * @param root
   * @return
   */
  public Map<Class<?>, List<String>> getSelectedFields(ResourceRegistry resourceRegistry, QuerySpec querySpec) {
    Map<Class<?>, List<String>> selectedFields = new HashMap<>();
    
    List<String> selectedFieldsOfThisClass = new ArrayList<>();

    // If no fields are specified, include all fields.
    if (querySpec.getIncludedFields().size() == 0) {
      selectedFieldsOfThisClass.addAll(
          resourceRegistry.getEntry(querySpec.getResourceClass())
              .getResourceInformation()
              .getAttributeFields()
              .stream()
              .map(ResourceField::getUnderlyingName)
              .collect(Collectors.toList())
      );
    } else {
      for (IncludeFieldSpec includedField : querySpec.getIncludedFields()) {
        selectedFieldsOfThisClass.add(String.join(".", includedField.getAttributePath()));
      }
    }

    // The id field is always selected, even if not explicitly requested by the user.
    String idAttribute = getIdAttribute(querySpec.getResourceClass(), resourceRegistry);
    if (!selectedFieldsOfThisClass.contains(idAttribute)) {
      selectedFieldsOfThisClass.add(idAttribute);
    }
    
    selectedFields.put(querySpec.getResourceClass(), selectedFieldsOfThisClass);
    
    for (QuerySpec nestedSpec : querySpec.getNestedSpecs()) {
      selectedFields.putAll(this.getSelectedFields(resourceRegistry, nestedSpec));
    }
    
    return selectedFields;
  }
  
  /**
   * Gets the name of the JPA entity's @Id attribute
   * 
   * @return
   */
  public String getIdAttribute(Class<?> resourceClass, ResourceRegistry resourceRegistry) {
    return resourceRegistry.findEntry(resourceClass)
        .getResourceInformation()
        .getIdField()
        .getUnderlyingName();
  }
  
}
