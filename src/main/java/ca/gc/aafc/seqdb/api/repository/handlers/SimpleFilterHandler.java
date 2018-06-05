package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.QuerySpec;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Simple filter handler for filtering by a value in a single attribute.
 * Example GET request where pcrPrimer's [name] == '101F' :
 *   http://localhost:8080/api/pcrPrimer?filter[name]=101F
 */
@Named
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SimpleFilterHandler implements FilterHandler {

  @NonNull
  private final SelectionHandler selectionHandler;

  @Override
  public Predicate getRestriction(QuerySpec querySpec, CriteriaBuilder criteriaBuilder,
      Root<?> root) {

    List<FilterSpec> filterSpecs = querySpec.getFilters();
    List<Predicate> predicates = new ArrayList<>();

    for (FilterSpec filterSpec : filterSpecs) {
      predicates.add(
          criteriaBuilder.equal(
              this.selectionHandler.getExpression(root, filterSpec.getAttributePath()),
              (Object) filterSpec.getValue()
          )
      );
    }

    return criteriaBuilder.and(predicates.stream().toArray(Predicate[]::new));
  }

}
