package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Streams;

import ca.gc.aafc.seqdb.api.repository.handlers.JpaDtoMapper;
import ca.gc.aafc.seqdb.api.repository.handlers.SelectionHandler;
import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.engine.information.resource.ResourceInformation;
import io.crnk.core.engine.internal.utils.PropertyUtils;
import io.crnk.core.engine.registry.ResourceRegistry;
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
  @Getter
  private final EntityManager entityManager;

  @NonNull
  @Getter
  private final SelectionHandler selectionHandler;

  @NonNull
  @Getter
  private final JpaDtoMapper dtoJpaMapper;

  private static final ModelMapper mapper = new ModelMapper();

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
      @Nullable TriFunction<From<?, ?>, CriteriaQuery<?>, CriteriaBuilder, Predicate> customFilter,
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
      criteriaQuery.where(customFilter.apply(targetPath, criteriaQuery, cb));
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
            .map(map -> JpaDtoRepository.mapper.map(map, targetDtoClass))
            .collect(Collectors.toList()),
        new MetaInformation() { },
        null
    );
  }

  /**
   * Update a JPA entity using a DTO.
   *
   * @param resource
   * @return the updated resource's ID
   */
  public Serializable save(Object resource, ResourceRegistry resourceRegistry) {
    // Get the entity of this DTO.
    Object entity = entityManager.find(
        dtoJpaMapper.getEntityClassForDto(resource.getClass()),
        PropertyUtils.getProperty(
            resource,
            selectionHandler.getIdAttribute(resource.getClass(), resourceRegistry)
        )
    );

    this.applyDtoToEntity(resource, entity, resourceRegistry);

    return (Serializable) entityManager.getEntityManagerFactory()
        .getPersistenceUnitUtil()
        .getIdentifier(entity);
  }

  /**
   * Persist a JPA entity using a DTO.
   * @param resource
   * @return the created resource's ID
   */
  public Serializable create(Object resource, ResourceRegistry resourceRegistry) {
    Object entity = (UniqueObj) BeanUtils.instantiate(
        this.dtoJpaMapper.getEntityClassForDto(resource.getClass())
    );

    this.applyDtoToEntity(resource, entity, resourceRegistry);

    entityManager.persist(entity);

    return (Serializable) entityManager.getEntityManagerFactory()
        .getPersistenceUnitUtil()
        .getIdentifier(entity);
  }

  /**
   * Deletes a JPA entity given a DTO.
   * 
   * @param resource the resource DTO to be deleted.
   * @param resourceRegistry the Crnk ResourceRegistry.
   */
  public void delete(Object resource, ResourceRegistry resourceRegistry) {
    Object entity = entityManager.find(
        this.dtoJpaMapper.getEntityClassForDto(resource.getClass()),
        resourceRegistry.findEntry(resource.getClass()).getResourceInformation().getId(resource)
    );
    entityManager.remove(entity);
  }

  /**
   * Modifies a relation
   * 
   * @param sourceEntity
   *          The source entity in the relation.
   * @param targetIds
   *          The IDs of the target entities to add/remove to the relation.
   * @param fieldName
   *          The name of the relation field on the source entity.
   * @param handleSourceCollectionAndTargetEntities
   *          When the source entity's relation field is a collection, how to handle the target
   *          entities (e.g. add or remove them to the collection).
   * @param handleOppositeCollectionAndSourceEntity
   *          When the target entity's relation field is a collection, how to handle the source
   *          entity (e.g. add or remove it to the collection).
   * @param handleTargetEntityAndFieldNameAndSourceEntity
   *          When the target entity's relation field is a singular reference, how to handle the
   *          source entity (e.g. set the target's field to reference the source entity).
   * @param resourceRegistry
   *          the Crnk ResourceRegistry
   */
  public void modifyRelation(
      @NonNull Object sourceEntity,
      @NonNull Iterable<Serializable> targetIds,
      @NonNull String fieldName,
      BiConsumer<Collection<Object>, Collection<Object>> handleSourceCollectionAndTargetEntities,
      BiConsumer<Collection<Object>, Object>  handleOppositeCollectionAndSourceEntity,
      TriConsumer<Object, String, Object> handleTargetEntityAndFieldNameAndSourceEntity,
      ResourceRegistry resourceRegistry
  ) {

    Class<?> entityClass = sourceEntity.getClass();
    Class<?> dtoClass = this.dtoJpaMapper.getDtoClassForEntity(entityClass);

    // Get the target resource DTO class
    Class<? extends Object> targetResourceClass = resourceRegistry.findEntry(dtoClass)
        .getResourceInformation()
        .findRelationshipFieldByName(fieldName)
        .getElementType();

    Collection<Object> targetEntities = Streams.stream(targetIds).map(
        id -> this.entityManager.find(
            this.dtoJpaMapper.getEntityClassForDto(targetResourceClass),
            id
        )
    ).collect(Collectors.toList());

    // Get the current value of the source object's relation field.
    Object sourceFieldValue = PropertyUtils.getProperty(
        sourceEntity,
        fieldName
    );

    // Handle a to-many or to-one relation.
    if (sourceFieldValue instanceof Collection) {
      @SuppressWarnings("unchecked")
      Collection<Object> sourceCollection = (Collection<Object>) sourceFieldValue;
      handleSourceCollectionAndTargetEntities.accept(
          (Collection<Object>) sourceCollection,
          targetEntities
      );
    } else {
      handleTargetEntityAndFieldNameAndSourceEntity.accept(
          sourceEntity,
          fieldName,
          targetEntities.iterator().hasNext() ? targetEntities.iterator().next() : null
      );
    }

    // In case of a bidirectional relation, get the opposite relation field name.
    String oppositeFieldName = resourceRegistry.findEntry(dtoClass)
        .getResourceInformation()
        .findFieldByName(fieldName)
        .getOppositeName();

    if (oppositeFieldName != null) {
      ResourceField oppositeField = resourceRegistry.findEntry(targetResourceClass)
          .getResourceInformation()
          .findFieldByName(oppositeFieldName);

      // Handle to-many or to-one relation from the opposite end of the bidirectional relation.
      if (oppositeField.isCollection()) {
        @SuppressWarnings("unchecked")
        List<Collection<Object>> oppositeCollections = targetEntities.stream()
            .map(targetEntity -> (Collection<Object>) PropertyUtils.getProperty(
                targetEntity,
                oppositeFieldName
             ))
            .collect(Collectors.toList());

        for (Collection<Object> oppositeCollection : oppositeCollections) {
          if (!oppositeCollection.contains(sourceEntity)) {
            handleOppositeCollectionAndSourceEntity.accept(oppositeCollection, sourceEntity);
          }
        }
      } else {
        for (Object targetEntity : targetEntities) {
          handleTargetEntityAndFieldNameAndSourceEntity.accept(
              targetEntity,
              oppositeFieldName,
              sourceEntity
          );
        }
      }
    }
  }

  /**
   * Gets a Map<String, Object> from a JPA Tuple. This is used to convert JPA's data-fetching output
   * to an object-graph-like structure that can more easily be deserialized to DTOs
   *
   * @param tuple
   * @return map
   */
  @SuppressWarnings("unchecked")
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

  /**
   * Apply the changed data held in a DTO object to a JPA entity.
   *
   * @param dto
   * @param entity
   * @param resourceRegistry
   */
  private void applyDtoToEntity(Object dto, Object entity, ResourceRegistry resourceRegistry) {
    ResourceInformation resourceInformation = resourceRegistry.findEntry(dto.getClass())
        .getResourceInformation();

    // Apply the DTO's attribute values to the entity.
    List<ResourceField> attributeFields = resourceInformation.getAttributeFields();
    for (ResourceField attributeField : attributeFields) {
      String attributeName = attributeField.getUnderlyingName();
      PropertyUtils.setProperty(
          entity,
          attributeName,
          PropertyUtils.getProperty(dto, attributeName)
      );
    }

    // Apply the DTO's relation values to the entity.
    List<ResourceField> relationFields = resourceInformation.getRelationshipFields();
    for (ResourceField relationField : relationFields) {
      String relationName = relationField.getUnderlyingName();

      // Get the ResourceInformation of the related element type.
      ResourceInformation relatedResourceInformation = resourceRegistry
          .findEntry(relationField.getElementType())
          .getResourceInformation();

      List<Serializable> targetIds = null;

      // Handle a to-many relation field.
      if (relationField.isCollection()) {
        // Get the collection of DTOs that specify the new collection elements.
        @SuppressWarnings("unchecked")
        Collection<Object> relatedResourceDtos = (Collection<Object>) PropertyUtils
            .getProperty(dto, relationName);

        // If the DTO collection is not null, this means that a new collection has been specified by
        // the client.
        if (relatedResourceDtos != null) {
          // Get the IDs of the collection elements.
          targetIds = relatedResourceDtos.stream()
              .map(relatedDto -> (Serializable) relatedResourceInformation.getId(relatedDto))
              .collect(Collectors.toList());
        }

      // Handle a to-one relation field.
      } else {
        Object relatedResourceDto = PropertyUtils.getProperty(dto, relationName);
        if (relatedResourceDto != null) {
          targetIds = Collections
              .singletonList((Serializable) relatedResourceInformation.getId(relatedResourceDto));
        } else {
          targetIds = new ArrayList<>();
        }
      }
      
      // Only modify relations if targetIds is specified. targetIds could be an empty list,
      // which would set a to-one relation to null. targetIds being null means that no change is
      // made.
      if (targetIds != null) {
        this.modifyRelation(
            entity,
            targetIds,
            relationName,
            (sourceCollection, targetEntities) -> {
              sourceCollection.clear();
              sourceCollection.addAll(targetEntities);
            },
            Collection::add,
            (targetEntity, oppositeFieldName, sourceEntity) -> PropertyUtils.setProperty(
                targetEntity,
                oppositeFieldName,
                sourceEntity
                ),
            resourceRegistry
        );
      }

    }
  }

  /**
   * An operation that accepts three input arguments and returns no result.
   *
   * @param <K> first argument type
   * @param <V> second argument type
   * @param <S> third argument type
   */
  public interface TriConsumer<A, B, C> {
    public void accept(A a, B b, C c);
  }
  
  /**
   * A function that accepts three input arguments and returns a result.
   * 
   * @param <A> first argument type
   * @param <B> second argument type
   * @param <C> third argument type
   * @param <R> return type
   */
  public interface TriFunction<A, B, C, R> {
    public R apply(A a, B b, C c);
  }

}
