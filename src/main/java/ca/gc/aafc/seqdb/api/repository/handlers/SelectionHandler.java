package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.BeanUtils;

import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.QuerySpec;

/**
 * Provides methods for handling sparse field sets.
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
  private Map<Class<?>, List<String>> getSelectedFieldsPerClass(ResourceRegistry resourceRegistry, QuerySpec querySpec) {
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
    
    // Add the selected fields for includes where sparse fields are requested.
    for (QuerySpec nestedSpec : querySpec.getNestedSpecs()) {
      selectedFields.putAll(this.getSelectedFieldsPerClass(resourceRegistry, nestedSpec));
    }
    
    // Add the selected fields for includes where fields are not selected for that type.
    for (IncludeRelationSpec rel : querySpec.getIncludedRelations()) {
      Class<?> relationClass = this.getType(querySpec.getResourceClass(), rel.getAttributePath());
      if (!selectedFields.containsKey(relationClass)) {
        selectedFields.putAll(this.getSelectedFieldsPerClass(resourceRegistry, new QuerySpec(relationClass)));
      }
    }
    
    return selectedFields;
  }
  
  public List<Selection<?>> getSelections(
      QuerySpec querySpec,
      Root<?> root,
      ResourceRegistry resourceRegistry
  ) {

    Map<Class<?>, List<String>> selectedFields = this.getSelectedFieldsPerClass(resourceRegistry, querySpec);
    
    List<Selection<?>> selections = new ArrayList<>();
    
    // Get the selections from the root resource.
    for (String selectedField : selectedFields.get(querySpec.getResourceClass())) {
      selections.add(
          root.get(selectedField)
              .alias(selectedField)
      );
    }
    
    // Loop through the "include"d relation paths to get the Selections for each relation.
    for (IncludeRelationSpec rel : querySpec.getIncludedRelations()) {
      From<?, ?> join = root;
      for (String pathElement : rel.getAttributePath()) {
        join = join.join(pathElement);
      }
      
      List<Selection<?>> includeSelections = new ArrayList<>();
      for (String selectedField : selectedFields.get(
          this.getType(querySpec.getResourceClass(), rel.getAttributePath())
      )) {
        includeSelections.add(
            join.get(selectedField).alias(String.join(".", rel.getAttributePath()) + "." + selectedField)
        );
      }
      selections.addAll(includeSelections);
      
    }

    return selections;
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
  
  private Class<?> getType(Class<?> baseType, List<String> attributePath) {
    Class<?> type = baseType;
    for (String pathElement : attributePath) {
      type = BeanUtils.getPropertyDescriptor(type, pathElement).getPropertyType();
    }
    return type;
  }
  
}
