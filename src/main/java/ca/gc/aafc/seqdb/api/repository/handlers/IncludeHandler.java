package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.BeanUtils;

import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.QuerySpec;

public class IncludeHandler {

  public List<Selection<?>> getSelections(
      QuerySpec querySpec,
      Root<?> root,
      Map<Class<?>, List<String>> selectedFields
  ) {

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
      From join = root;
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
  
  private Class<?> getType(Class<?> baseType, List<String> attributePath) {
    Class<?> type = baseType;
    for (String pathElement : attributePath) {
      type = BeanUtils.getPropertyDescriptor(type, pathElement).getPropertyType();
    }
    return type;
  }

}
