package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.modelmapper.ModelMapper;

import ca.gc.aafc.seqdb.api.repository.handlers.SelectionHandler;
import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.engine.registry.ResourceRegistryAware;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * JsonApi repository that interfaces using DTOs, and uses JPA entities internally.
 *
 * @param <D> the JsonApi DTO class.
 * @param <E> the JPA entity class.
 */
@RequiredArgsConstructor
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

  @NonNull
  private final EntityManager entityManager;
  
  @NonNull
  private final SelectionHandler selectionHandler;
  
  @Setter(onMethod_ = @Override)
  private ResourceRegistry resourceRegistry;
  
  private final ModelMapper mapper = new ModelMapper();

  @Override
  public D findOne(Serializable id, QuerySpec querySpec) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
    Root<E> root = criteriaQuery.from(entityClass);

    // Filter by entity id attribute.
    criteriaQuery.where(
        cb.equal(
            root.get(this.selectionHandler.getIdAttribute(resourceClass, resourceRegistry)),
            id
        )
    );

    List<Selection<?>> selections = this.selectionHandler.getSelections(querySpec, root, resourceRegistry);
    
    criteriaQuery.multiselect(selections);

    Tuple entityResult;
    try {
      entityResult = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch (NoResultException e) {
      throw new ResourceNotFoundException(e.getMessage());
    }

    Map<String, Object> resultMap = new HashMap<>();
    for (TupleElement<?> element : entityResult.getElements()) {
      Object value = entityResult.get(element);
      if (value == null) {
        continue;
      }
      Map<String, Object> currentNode = resultMap;
      List<String> attributePath = Arrays.asList(element.getAlias().split("\\."));
      for (int i = 0; i < attributePath.size() - 1; i++) {
        String pathElement = attributePath.get(i);
        if (!currentNode.containsKey(pathElement)) {
          currentNode.put(pathElement, new HashMap<>());
        }
        currentNode = (Map<String, Object>) currentNode.get(pathElement);
      }
      currentNode.put(attributePath.get(attributePath.size() - 1), value);
    }
    
    return mapper.map(resultMap, this.resourceClass);
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
