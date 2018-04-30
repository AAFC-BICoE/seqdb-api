package ca.gc.aafc.seqdb.api.repository.handlers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.QuerySpec;
import lombok.NonNull;

/**
 * Provides methods for handling sparse field sets.
 */
public class FieldsHandler {
  
  private final Class<?> resourceClass;
  
  private final Class<?> entityClass;
  
  private final EntityManager entityManager;
  
//  private final Set<String> resourceFields;
//  private final Set<String> resourceRelationships;
  
  public FieldsHandler(
      @NonNull Class<?> resourceClass,
      @NonNull Class<?> entityClass,
      @NonNull EntityManager entityManager
  ) {
    this.resourceClass = resourceClass;
    this.entityClass = entityClass;
    this.entityManager = entityManager;
  }
  
  /**
   * Gets the selected fields as strings from the querySpec.
   * 
   * @param querySpec
   * @param root
   * @return
   */
  public List<String> getSelectedFields(ResourceRegistry resourceRegistry, QuerySpec querySpec, Root<?> root) {
    List<String> selectedFields = new ArrayList<>();

    // If no fields are specified, include all fields.
    if (querySpec.getIncludedFields().size() == 0) {
      selectedFields.addAll(
          resourceRegistry.getEntry(this.resourceClass)
              .getResourceInformation()
              .getAttributeFields()
              .stream()
              .map(ResourceField::getUnderlyingName)
              .collect(Collectors.toList())
      );
    } else {
      for (IncludeFieldSpec includedField : querySpec.getIncludedFields()) {
        selectedFields.add(includedField.getAttributePath().get(0));
      }
    }
    
    // The id field is always selected, even if not explicitly requested by the user.
    String entityIdAttribute = getEntityIdAttribute();
    if (!selectedFields.contains(entityIdAttribute)) {
      selectedFields.add(entityIdAttribute);
    }
    
    return selectedFields;
  }
  
  /**
   * Gets the JPA criteria query selections from a list of DTO properties.
   * 
   * @param selectedFields
   * @param root
   * @return
   */
  public Selection<?>[] getSelections(List<String> selectedFields, Root<?> root) {
    return selectedFields
        .stream()
        .map(root::get)
        .toArray(Selection[]::new);
  }
  
  /**
   * Gets the name of the JPA entity's @Id attribute
   * 
   * @return
   */
  public String getEntityIdAttribute() {
    return entityManager
        .getMetamodel()
        .entity(entityClass)
        .getId(Serializable.class)
        .getName();
  }
  
}
