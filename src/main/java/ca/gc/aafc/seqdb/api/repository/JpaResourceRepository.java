package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;

import ca.gc.aafc.seqdb.api.repository.handlers.ExpressionHandler;
import ca.gc.aafc.seqdb.api.repository.handlers.SelectionHandler;
import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.engine.registry.ResourceRegistryAware;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.Direction;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.links.DefaultPagedLinksInformation;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;
import io.crnk.core.resource.meta.DefaultPagedMetaInformation;
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
  
  @NonNull
  private final ExpressionHandler expressionHandler;
  
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
            this.expressionHandler.getIdExpression(root, resourceClass, resourceRegistry),
            id
        )
    );

    criteriaQuery.multiselect(this.selectionHandler.getSelections(querySpec, root, resourceRegistry));

    Tuple result;
    try {
      result = entityManager.createQuery(criteriaQuery).getSingleResult();
    } catch (NoResultException e) {
      throw new ResourceNotFoundException(e.getMessage());
    }

    Map<String, Object> resultMap = JpaResourceRepository.mapFromTuple(result);
    
    return mapper.map(resultMap, this.resourceClass);
  }

  @Override
  public ResourceList<D> findAll(QuerySpec querySpec) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
    Root<E> root = criteriaQuery.from(entityClass);
    
    criteriaQuery.multiselect(this.selectionHandler.getSelections(querySpec, root, resourceRegistry));

    List<Order> orders = querySpec.getSort().stream().map(sort -> {
      Function<Expression<?>, Order> orderFunc = sort.getDirection() == Direction.ASC ? cb::asc : cb::desc;
      return orderFunc.apply(this.expressionHandler.getExpression(root, sort.getAttributePath()));
    })
    .collect(Collectors.toList());
    
    criteriaQuery.orderBy(orders);
    
    List<Tuple> result = entityManager
        .createQuery(criteriaQuery)
        .setFirstResult(Optional.ofNullable(querySpec.getOffset()).orElse(Long.valueOf(0)).intValue())
        .setMaxResults(Optional.ofNullable(querySpec.getLimit()).orElse(Long.valueOf(100)).intValue())
        .getResultList();
    
    return new DefaultResourceList<>(
        result.stream()
            .map(JpaResourceRepository::mapFromTuple)
            .map(map -> this.mapper.map(map, this.resourceClass))
            .collect(Collectors.toList()),
        new DefaultPagedMetaInformation(),
        new DefaultPagedLinksInformation()
    );
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
    E entity = mapper.map(resource, entityClass);
    entityManager.persist(entity);
    return (S) this.findOne(entity.getId(), new QuerySpec(resourceClass));
  }

  @Override
  public void delete(Serializable id) {
    E entity = entityManager.find(this.entityClass, id);
    if (entity == null) {
      throw new ResourceNotFoundException("Resource not found");
    }
    entityManager.remove(entity);
  }
  
  private static Map<String, Object> mapFromTuple(Tuple tuple) {
    Map<String, Object> map = new HashMap<>();
    for (TupleElement<?> element : tuple.getElements()) {
      Object value = tuple.get(element);
      if (value == null) {
        continue;
      }
      Map<String, Object> currentNode = map;
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
    return map;
  }

}
