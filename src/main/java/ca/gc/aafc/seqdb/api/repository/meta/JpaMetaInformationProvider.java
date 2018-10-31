package ca.gc.aafc.seqdb.api.repository.meta;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.meta.MetaInformation;

/**
 * Interface for providing JSONAPI response meta information given a JPA Criteria Query.
 */
public interface JpaMetaInformationProvider {

  /**
   * Provides JSONAPI MetaInformation given a JPA Criteria Query.
   * 
   * @param querySpec The Crnk QuerySpec.
   * @param from The path for the query's target entity.
   * @param query The existing criteria query.
   * @param cb The JPA CriteriaBuilder.
   * @return The JSONAPI MetaInformation.
   */
  public MetaInformation getMetaInformation(
      QuerySpec querySpec,
      From<?, ?> from,
      CriteriaQuery<?> query,
      CriteriaBuilder cb
  );
  
}
