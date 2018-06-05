package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Named;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.BeanUtils;

import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.QuerySpec;

/**
 * Provides methods for handling sparse field sets and inclusion of related resources.
 */
@Named
public class SelectionHandler {
  
  /**
   * Gets the selected fields as strings from the querySpec.
   * 
   * @param querySpec
   * @param root
   * @return
   */
  private Map<Class<?>, List<List<String>>> getSelectedFieldsPerClass(
      ResourceRegistry resourceRegistry, QuerySpec querySpec) {
    Map<Class<?>, List<List<String>>> selectedFields = new HashMap<>();
    
    List<List<String>> selectedFieldsOfThisClass = new ArrayList<>();

    // If no fields are specified, include all fields.
    if (querySpec.getIncludedFields().size() == 0) {
      selectedFieldsOfThisClass.addAll(
          resourceRegistry.getEntry(querySpec.getResourceClass())
              .getResourceInformation()
              .getAttributeFields()
              .stream()
              .map(ResourceField::getUnderlyingName)
              .map(Collections::singletonList)
              .collect(Collectors.toList())
      );
    } else {
      for (IncludeFieldSpec includedField : querySpec.getIncludedFields()) {
        selectedFieldsOfThisClass.add(includedField.getAttributePath());
      }
    }

    // The id field is always selected, even if not explicitly requested by the user.
    String idAttribute = getIdAttribute(querySpec.getResourceClass(), resourceRegistry);
    if (!selectedFieldsOfThisClass.contains(Collections.singletonList(idAttribute))) {
      selectedFieldsOfThisClass.add(Collections.singletonList(idAttribute));
    }
    
    selectedFields.put(querySpec.getResourceClass(), selectedFieldsOfThisClass);
    
    // Add the selected fields for includes where sparse fields are requested.
    for (QuerySpec nestedSpec : querySpec.getNestedSpecs()) {
      selectedFields.putAll(this.getSelectedFieldsPerClass(resourceRegistry, nestedSpec));
    }
    
    // Add the selected fields for includes where fields are not selected for that type.
    for (IncludeRelationSpec rel : querySpec.getIncludedRelations()) {
      Class<?> relationClass = this.getType(querySpec.getResourceClass(), rel.getAttributePath());
      if (!selectedFields.containsKey(relationClass)) {
        selectedFields
            .putAll(this.getSelectedFieldsPerClass(resourceRegistry, new QuerySpec(relationClass)));
      }
    }
    
    return selectedFields;
  }
  
  public List<Selection<?>> getSelections(
      QuerySpec querySpec,
      Root<?> root,
      ResourceRegistry resourceRegistry
  ) {

    Map<Class<?>, List<List<String>>> selectedFields = this
        .getSelectedFieldsPerClass(resourceRegistry, querySpec);
    
    List<Selection<?>> selections = new ArrayList<>();
    
    // Get the selections from the root resource.
    for (List<String> selectedAttributePath : selectedFields.get(querySpec.getResourceClass())) {
      selections.add(
          this.getExpression(root, selectedAttributePath)
              .alias(String.join(".", selectedAttributePath))
      );
    }
    
    // Loop through the "include"d relation paths to get the Selections for each relation.
    for (IncludeRelationSpec rel : querySpec.getIncludedRelations()) {
      List<Selection<?>> includeSelections = new ArrayList<>();
      for (List<String> selectedField : selectedFields.get(
          this.getType(querySpec.getResourceClass(), rel.getAttributePath())
      )) {
        List<String> fieldPath = Stream
            .concat(rel.getAttributePath().stream(), selectedField.stream())
            .collect(Collectors.toList());
        includeSelections.add(
            this.getExpression(
                root,
                fieldPath
            )
            .alias(String.join(".", fieldPath))
        );
      }
      selections.addAll(includeSelections);
      
    }

    return selections;
  }
  
  public Expression<?> getExpression(From<?, ?> root, List<String> attributePath) {
    From<?,?> from = root;
    for (String pathElement : attributePath.subList(0, attributePath.size() - 1)) {
      from = from.join(pathElement, JoinType.LEFT);
    }
    return from.get(attributePath.get(attributePath.size() - 1));
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
  
  /**
   * Gets the resource class' @JsonApiId attribute.
   * 
   * @return the JPA Expression of the Id attribute.
   */
  public Expression<?> getIdExpression(Root<?> root, Class<?> resourceClass, ResourceRegistry resourceRegistry) {
    return root.get(this.getIdAttribute(resourceClass, resourceRegistry));
  }
  
  private Class<?> getType(Class<?> baseType, List<String> attributePath) {
    Class<?> type = baseType;
    for (String pathElement : attributePath) {
      type = BeanUtils.getPropertyDescriptor(type, pathElement).getPropertyType();
    }
    return type;
  }
  
}
