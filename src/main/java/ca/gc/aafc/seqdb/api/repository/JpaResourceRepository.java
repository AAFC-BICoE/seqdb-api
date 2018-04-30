package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import ca.gc.aafc.seqdb.api.repository.handlers.FieldsHandler;
import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.engine.registry.ResourceRegistryAware;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * JsonApi repository that interfaces using DTOs, and uses JPA entities internally.
 *
 * @param <D> the JsonApi DTO class.
 * @param <E> the JPA entity class.
 */
public class JpaResourceRepository<D, E extends UniqueObj>
    implements ResourceRepositoryV2<D, Serializable>, ResourceRegistryAware {

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

  private final EntityManager entityManager;
  
  private final FieldsHandler fieldsHandler;
  
  @Setter(onMethod_ = @Override)
  private ResourceRegistry resourceRegistry;
  
  private final ModelMapper mapper = new ModelMapper();
  
  public JpaResourceRepository(
    @NonNull Class<D> resourceClass,
    @NonNull Class<E> entityClass,
    @NonNull EntityManager entityManager
  ) {
    this.resourceClass = resourceClass;
    this.entityClass = entityClass;
    this.entityManager = entityManager;
    
    this.fieldsHandler = new FieldsHandler(resourceClass, entityClass, entityManager);
  }

  @Override
  public D findOne(Serializable id, QuerySpec querySpec) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
    Root<E> root = criteriaQuery.from(entityClass);

    // Filter by entity id attribute.
    criteriaQuery.where(cb.equal(root.get(this.fieldsHandler.getEntityIdAttribute()), id));

    List<String> selectedFields = this.fieldsHandler.getSelectedFields(resourceRegistry, querySpec, root);
    
    criteriaQuery.multiselect(this.fieldsHandler.getSelections(selectedFields, root));

    Tuple entityResult;
    try {
      entityResult = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch (NoResultException e) {
      throw new ResourceNotFoundException(e.getMessage());
    }

    DocumentContext result = JsonPath.parse(new HashMap<>());

    for (int i = 0; i < selectedFields.size(); i++) {
      result.put(
          "$",
          selectedFields.get(i),
          entityResult.get(i)
      );
    }

    return mapper.map(result.json(), resourceClass);
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
    E entity = entityManager.find(this.entityClass, id);
    if (entity == null) {
      throw new ResourceNotFoundException("Resource not found");
    }
    entityManager.remove(entity);
  }

}
