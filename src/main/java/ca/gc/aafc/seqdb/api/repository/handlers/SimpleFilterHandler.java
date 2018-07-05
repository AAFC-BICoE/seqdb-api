package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

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
  public Predicate getRestriction(
      QuerySpec querySpec,
      From<?, ?> root,
      CriteriaQuery<?> query,
      CriteriaBuilder cb
  ) {

    List<FilterSpec> filterSpecs = querySpec.getFilters();
    List<Predicate> predicates = new ArrayList<>();

    for (FilterSpec filterSpec : filterSpecs) {
      predicates.add(
          cb.equal(
              this.selectionHandler.getExpression(root, filterSpec.getAttributePath()),
              (Object) filterSpec.getValue()
          )
      );
    }

    return cb.and(predicates.stream().toArray(Predicate[]::new));
  }

}
