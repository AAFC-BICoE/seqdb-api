package ca.gc.aafc.seqdb.api.repository.handlers;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

import io.crnk.core.queryspec.QuerySpec;

/**
 * Interface for creating custom filtering strategies for the JpaResourceRepository.
 */
public interface FilterHandler {
  public Predicate getRestriction(QuerySpec querySpec, CriteriaBuilder criteriaBuilder, From<?,?> root);
}
