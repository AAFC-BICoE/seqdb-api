package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.modelmapper.ModelMapper;

import ca.gc.aafc.seqdb.api.dto.JsonApiDto;
import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * JsonApi repository that interfaces using DTOs, and uses JPA entities internally.
 *
 * @param <D> the JsonApi DTO class.
 * @param <E> the JPA entity class.
 */
@RequiredArgsConstructor
public class JpaResourceRepository<D extends JsonApiDto, E extends UniqueObj>
    implements ResourceRepositoryV2<D, Serializable> {

  /**
   * The JsonApi resource class.
   */
  @Getter
  private final Class<D> resourceClass;

  /**
   * The JPA entity class.
   */
  @Getter
  private final Class<E> entityClass;

  @PersistenceContext
  private final EntityManager entityManager;

  private final ModelMapper mapper = new ModelMapper();

  @Override
  public D findOne(Serializable id, QuerySpec querySpec) {
    
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> criteriaQuery = cb.createQuery(entityClass);
    criteriaQuery.from(entityClass);
    
    E entityResult;
    try {
      entityResult = entityManager.createQuery(criteriaQuery).getSingleResult();
    }
    catch (NoResultException e) {
      throw new ResourceNotFoundException(e.getMessage());
    }
    
    return mapper.map(entityResult, resourceClass);
  }

  @Override
  public ResourceList<D> findAll(QuerySpec querySpec) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResourceList<D> findAll(Iterable<Serializable> ids, QuerySpec querySpec) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends D> S save(S resource) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends D> S create(S resource) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(Serializable id) {
    entityManager.remove(entityManager.find(entityClass, id));
  }

}
