package ca.gc.aafc.seqdb.api.repository.meta;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.meta.DefaultPagedMetaInformation;
import lombok.RequiredArgsConstructor;

/**
 * Uses an existing JPA criteria query to provide a DefaultPagedMetaInformation containing the total
 * count of resources that match the existing query's restrictions.
 */
@Named
@RequiredArgsConstructor
public class JpaTotalMetaInformationProvider implements JpaMetaInformationProvider {

  private final EntityManager entityManager;

  @Override
  public DefaultPagedMetaInformation getMetaInformation(
      QuerySpec querySpec,
      From<?, ?> from,
      CriteriaQuery<?> resourceQuery,
      CriteriaBuilder cb
  ) {
    // Create the total count query.
    CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
    from = countQuery.from(from.getJavaType());
    countQuery.select(cb.count(from));

    // Use the same restrictions as the existing entity query.
    countQuery.where(resourceQuery.getRestriction());

    // Get the total.
    Long total = this.entityManager.createQuery(countQuery).getSingleResult();

    // Return the DefaultPagedMetaInformation with the total.
    DefaultPagedMetaInformation metaInformation = new DefaultPagedMetaInformation();
    metaInformation.setTotalResourceCount(total);
    return metaInformation;
  }

}
