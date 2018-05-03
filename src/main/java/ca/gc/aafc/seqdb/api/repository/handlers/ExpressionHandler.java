package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.List;

import javax.inject.Named;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import io.crnk.core.engine.registry.ResourceRegistry;

@Named
public class ExpressionHandler {
  
  public Expression<?> getExpression(From<?, ?> root, List<String> attributePath) {
    From<?,?> from = root;
    for (String pathElement : attributePath.subList(0, attributePath.size() - 1)) {
      from = from.join(pathElement, JoinType.LEFT);
    }
    return from.get(attributePath.get(attributePath.size() - 1));
  }
  
  /**
   * Gets the resource class' @JsonApiId attribute.
   * 
   * @return the JPA Expression of the Id attribute.
   */
  public Expression<?> getIdExpression(Root<?> root, Class<?> resourceClass, ResourceRegistry resourceRegistry) {
    return root.get(
        resourceRegistry.findEntry(resourceClass)
            .getResourceInformation()
            .getIdField()
            .getUnderlyingName()
    );
  }
  
}
