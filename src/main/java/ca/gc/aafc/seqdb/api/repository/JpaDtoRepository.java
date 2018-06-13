package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.seqdb.api.repository.handlers.DtoJpaMapper;
import ca.gc.aafc.seqdb.api.repository.handlers.SelectionHandler;
import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.engine.internal.utils.PropertyUtils;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.Direction;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;
import io.crnk.core.resource.meta.MetaInformation;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class JpaDtoRepository {

  @NonNull
  private final EntityManager entityManager;

  @NonNull
  private final SelectionHandler selectionHandler;

  @NonNull
  @Getter
  private final DtoJpaMapper dtoJpaMapper;

  private ModelMapper mapper = new ModelMapper();

  /**
   * Query the DTO repository backed by a JPA datasource for a list of DTOs.
   *
   * @param sourceDtoClass
   *          the source DTO class. It will be PcrBatchDto for GET /pcrBatch/1 , and it will be
   *          PcrBatchDto for /pcrBatch/1/reactions .
   * @param querySpec
   *          the crnk QuerySpec
   * @param resourceRegistry
   *          the crnk ResourceRegistry
   * @param customFilter
   *          custom JPA filter
   * @param customRoot
   *          function to change the root path of the query. E.g. when searching related elements in
   *          a request like localhost:8080/api/pcrBatch/10/reactions
   * @return the resource list
   */
  public <D> ResourceList<D> findAll(
      @NonNull Class<D> sourceDtoClass,
      @NonNull QuerySpec querySpec,
      @NonNull ResourceRegistry resourceRegistry,
      @Nullable BiFunction<From<?, ?>, CriteriaBuilder, Predicate> customFilter,
      @Nullable Function<From<?, ?>, From<?, ?>> customRoot
  ) {
    @SuppressWarnings("unchecked")
    Class<D> targetDtoClass = (Class<D>) querySpec.getResourceClass();

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
    From<?, ?> sourcePath = criteriaQuery.from(dtoJpaMapper.getEntityClassForDto(sourceDtoClass));

    From<?, ?> targetPath = customRoot != null ? customRoot.apply(sourcePath) : sourcePath;

    criteriaQuery.multiselect(
        this.selectionHandler.getSelections(querySpec, targetPath, resourceRegistry)
    );

    List<Order> orders = querySpec.getSort().stream().map(sort -> {
      Function<Expression<?>, Order> orderFunc =
          sort.getDirection() == Direction.ASC ? cb::asc : cb::desc;
      return orderFunc.apply(
          this.selectionHandler.getExpression(targetPath, sort.getAttributePath())
      );
    }).collect(Collectors.toList());

    criteriaQuery.orderBy(orders);

    // Add the custom filter to the criteria query.
    if (customFilter != null) {
      criteriaQuery.where(customFilter.apply(targetPath, cb));
    }

    List<Tuple> result = entityManager
        .createQuery(criteriaQuery)
        .setFirstResult(
            Optional.ofNullable(querySpec.getOffset()).orElse(Long.valueOf(0)).intValue()
        )
        .setMaxResults(
            Optional.ofNullable(querySpec.getLimit()).orElse(Long.valueOf(100)).intValue()
        )
        .getResultList();

    return new DefaultResourceList<>(
        result.stream()
            .map(JpaDtoRepository::mapFromTuple)
            .map(map -> this.mapper.map(map, targetDtoClass))
            .collect(Collectors.toList()),
        new MetaInformation() {},
        null
    );
  }

  /**
   * Update a JPA entity using a DTO.
   * 
   * @param resource
   * @return the updated resource
   */
  public <D> D save(D resource, ResourceRegistry resourceRegistry) {
    // Get the entity of this DTO.
    Object entity = entityManager.find(
        dtoJpaMapper.getEntityClassForDto(resource.getClass()),
        PropertyUtils.getProperty(
            resource,
            selectionHandler.getIdAttribute(resource.getClass(), resourceRegistry)
        )
    );
    
    // Apply the DTO's attribute values to the entity.
    List<ResourceField> attributeFields = resourceRegistry.findEntry(resource.getClass())
        .getResourceInformation()
        .getAttributeFields();
    for (ResourceField attributeField : attributeFields) {
      String attributeName = attributeField.getUnderlyingName();
      PropertyUtils.setProperty(entity, attributeName, PropertyUtils.getProperty(resource, attributeName));
    }
    
    // Return the modified resource.
    return resource;
  }

  /**
   * Persist a JPA entity using a DTO.
   * @param resource
   * @return the created resource
   */
  public <D> D create(D resource) {
    UniqueObj entity = (UniqueObj) mapper.map(
        resource,
        this.dtoJpaMapper.getEntityClassForDto(resource.getClass())
    );
    entityManager.persist(entity);

    @SuppressWarnings("unchecked")
    D result = (D) this.mapper.map(
        entity,
        resource.getClass()
    );

    return result;
  }

  /**
   * Delete a JPA entity.
   * @param dtoClass the JPA entity's DTO class
   * @param id the entity's ID
   */
  public void delete(Class<?> dtoClass, Serializable id) {
    Object entity = entityManager.find(
        this.dtoJpaMapper.getEntityClassForDto(dtoClass),
        id
    );
    if (entity == null) {
      throw new ResourceNotFoundException("Resource not found");
    }
    entityManager.remove(entity);
  }

  /**
   * Gets a Map<String, Object> from a JPA Tuple. This is used to convert JPA's data-fetching output
   * to an object-graph-like structure that can more easily be deserialized to DTOs
   *
   * @param tuple
   * @return map
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> mapFromTuple(Tuple tuple) {
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
