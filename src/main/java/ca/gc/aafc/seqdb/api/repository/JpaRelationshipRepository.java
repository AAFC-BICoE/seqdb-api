package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import com.google.common.collect.Streams;

import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
import io.crnk.core.engine.information.resource.ResourceField;
import io.crnk.core.engine.internal.utils.PropertyUtils;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.engine.registry.ResourceRegistryAware;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.RelationshipRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * JSONAPI repository that interfaces using DTOs, and uses JPA entities internally.
 *
 * @param <S> the source resource
 * @param <T> the target resource
 */
@Transactional
@RequiredArgsConstructor
public class JpaRelationshipRepository<S, T>
    implements RelationshipRepositoryV2<S, Serializable, T, Serializable>, ResourceRegistryAware {

  @NonNull
  @Getter(onMethod_ = @Override)
  private final Class<S> sourceResourceClass;

  @NonNull
  @Getter(onMethod_ = @Override)
  private final Class<T> targetResourceClass;

  @NonNull
  private final JpaDtoRepository dtoRepository;

  @NonNull
  private final FilterHandler filterHandler;

  @Setter(onMethod_ = @Override)
  private ResourceRegistry resourceRegistry;

  @Override
  public void setRelation(S source, Serializable targetId, String fieldName) {
    this.modifyRelation(
        source,
        Collections.singletonList(targetId),
        fieldName,
        null,
        Collection::add,
        (targetEntity, oppositeFieldName, sourceEntity) -> PropertyUtils.setProperty(
            targetEntity,
            oppositeFieldName,
            sourceEntity
        )
    );
  }

  @Override
  public void setRelations(S source, Iterable<Serializable> targetIds, String fieldName) {
    this.modifyRelation(
        source,
        targetIds,
        fieldName,
        (sourceCollection, targetEntities) -> {
          sourceCollection.clear();
          sourceCollection.addAll(targetEntities);
        },
        Collection::add,
        (targetEntity, oppositeFieldName, sourceEntity) -> PropertyUtils.setProperty(
            targetEntity,
            oppositeFieldName,
            sourceEntity
        )
    );
  }

  @Override
  public void addRelations(S source, Iterable<Serializable> targetIds, String fieldName) {
    this.modifyRelation(
        source,
        targetIds,
        fieldName,
        Collection::addAll,
        Collection::add,
        (targetEntity, oppositeFieldName, sourceEntity) -> PropertyUtils.setProperty(
            targetEntity,
            oppositeFieldName,
            sourceEntity
        )
    );
  }

  @Override
  public void removeRelations(S source, Iterable<Serializable> targetIds, String fieldName) {
    this.modifyRelation(
        source,
        targetIds,
        fieldName,
        Collection::removeAll,
        Collection::remove,
        (targetEntity, oppositeFieldName, sourceEntity) -> PropertyUtils.setProperty(
            targetEntity,
            oppositeFieldName,
            null
        )
    );
  }

  @Override
  public T findOneTarget(Serializable sourceId, String fieldName, QuerySpec targetQuerySpec) {
    // Use the findManyTargets method, but limit the result size to 1.
    targetQuerySpec.setLimit(Long.valueOf(1));
    ResourceList<T> resultSet = this.findManyTargets(sourceId, fieldName, targetQuerySpec);

    // Throw the 404 exception if the resource is not found.
    if (resultSet.size() == 0) {
      throw new ResourceNotFoundException("");
    }

    // There should only be one result element in the list.
    return resultSet.get(0);
  }

  @Override
  public ResourceList<T> findManyTargets(Serializable sourceId, String fieldName,
      QuerySpec querySpec) {

    // The source entity has the to-many relationship.
    Class<?> sourceEntityClass = dtoRepository.getDtoJpaMapper()
        .getEntityClassForDto(sourceResourceClass);

    // Wrapper array to hold reference to the source entity's JPA path. Lambda scoping prevents this
    // from being a regular variable.
    From<?, ?>[] sourcePathHolder = new From<?, ?>[1];

    @SuppressWarnings("unchecked")
    ResourceList<T> resultSet = (ResourceList<T>) dtoRepository.findAll(
        sourceResourceClass, querySpec, resourceRegistry,
        // Add the filters to the target entity's path.
        (targetPath, cb) -> {
          From<?, ?> sourcePath = sourcePathHolder[0];

          List<Predicate> restrictions = new ArrayList<>();

          // Add the filter handler's restriction.
          restrictions.add(this.filterHandler.getRestriction(querySpec, cb, targetPath));

          // Restrict the source entity to the given sourceId.
          restrictions.add(
              cb.equal(
                  sourcePath.get(
                      dtoRepository.getEntityManager()
                          .getMetamodel()
                          .entity(sourceEntityClass)
                          .getId(Serializable.class)
                  ),
                  sourceId
              )
          );

          // Combine predicates in an 'and' operation.
          return cb.and(restrictions.stream().toArray(Predicate[]::new));
        },
        sourcePath -> {
          // Get the reference to the source entity's path.
          sourcePathHolder[0] = sourcePath;
          // Create the Join to the target entity.
          return sourcePath.join(fieldName);
        }
    );

    return resultSet;
  }

  private Object getEntityFromDto(Object dto) {
    return this.dtoRepository.getEntityManager().find(
        this.dtoRepository.getDtoJpaMapper().getEntityClassForDto(dto.getClass()),
        this.resourceRegistry.findEntry(this.sourceResourceClass)
            .getResourceInformation()
            .getId(dto)
    );
  }

  private List<Object> getEntitiesFromTargetIds(Iterable<Serializable> ids) {
    return Streams.stream(ids).map(
        id -> this.dtoRepository.getEntityManager().find(
            this.dtoRepository.getDtoJpaMapper().getEntityClassForDto(this.targetResourceClass),
            id
        )
    ).collect(Collectors.toList());
  }

  /**
   * Contains common logic for relation-modifying methods.
   * 
   * @param sourceDto source DTO
   * @param targetIds Target resource IDs
   * @param fieldName relation field name
   * @param handleSourceCollectionAndTargetEntities
   * @param handleOppositeCollectionAndSourceEntity
   * @param handleTargetEntityAndFieldNameAndSourceEntity
   */
  private void modifyRelation(
      Object sourceDto,
      Iterable<Serializable> targetIds,
      String fieldName,
      BiConsumer<Collection<Object>, Collection<Object>> handleSourceCollectionAndTargetEntities,
      BiConsumer<Collection<Object>, Object>  handleOppositeCollectionAndSourceEntity,
      TriConsumer<Object, String, Object> handleTargetEntityAndFieldNameAndSourceEntity
  ) {
    
    // Get the source an target entities.
    Object sourceEntity = this.getEntityFromDto(sourceDto);
    Collection<Object> targetEntities = this.getEntitiesFromTargetIds(targetIds);

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
          targetEntities.iterator().next()
      );
    }

    // In case of a bidirectional relation, get the opposite relation field name.
    String oppositeFieldName = this.resourceRegistry.findEntry(this.sourceResourceClass)
        .getResourceInformation()
        .findFieldByName(fieldName)
        .getOppositeName();

    if (oppositeFieldName != null) {
      ResourceField oppositeField = this.resourceRegistry.findEntry(this.targetResourceClass)
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
   * An operation that accepts three input arguments and returns no result.
   *
   * @param <K> type of the first argument
   * @param <V> type of the second argument
   * @param <S> type of the third argument
   */
  private interface TriConsumer<K, V, S> {
    void accept(K k, V v, S s);
  }

}
