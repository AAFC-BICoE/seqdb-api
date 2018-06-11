package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;

import ca.gc.aafc.seqdb.api.repository.handlers.DtoJpaMapper;
import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
import ca.gc.aafc.seqdb.api.repository.handlers.SelectionHandler;
import io.crnk.core.engine.internal.utils.PropertyUtils;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.engine.registry.ResourceRegistryAware;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.Direction;
import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.RelationshipRepositoryV2;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.DefaultResourceList;
import io.crnk.core.resource.list.ResourceList;
import io.crnk.core.resource.meta.DefaultPagedMetaInformation;
import io.crnk.core.resource.meta.PagedMetaInformation;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class JpaRelationshipRepository<S, T>
    implements RelationshipRepositoryV2<S, Serializable, T, Serializable>, ResourceRegistryAware {

  @NonNull
  @Getter(onMethod_ = @Override)
  private final Class<S> sourceResourceClass;
  
  @NonNull
  @Getter(onMethod_ = @Override)
  private final Class<T> targetResourceClass;
  
  @Setter(onMethod_ = @Override)
  private ResourceRegistry resourceRegistry;
  
  @NonNull
  private final EntityManager entityManager;
  
  @NonNull
  private final DtoJpaMapper dtoJpaMapper;
  
  @NonNull
  private final SelectionHandler selectionHandler;
  
  @NonNull
  private final FilterHandler filterHandler;
  
  private final ModelMapper mapper = new ModelMapper();
  
  @Override
  public void setRelation(S source, Serializable targetId, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRelations(S source, Iterable<Serializable> targetIds, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addRelations(S source, Iterable<Serializable> targetIds, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeRelations(S source, Iterable<Serializable> targetIds, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public T findOneTarget(Serializable sourceId, String fieldName, QuerySpec targetQuerySpec) {
    // Get the facade of the source repository.
    ResourceRepositoryV2<S, Serializable> sourceResourceRepository = resourceRegistry
        .findEntry(this.sourceResourceClass).getResourceRepositoryFacade();
    
    // Create a new QuerySpec for the source resource with the requested relation as a nested
    // QuerySpec.
    QuerySpec sourceQuerySpec = new QuerySpec(this.sourceResourceClass);
    sourceQuerySpec.setIncludedRelations(
        Collections.singletonList(new IncludeRelationSpec(Arrays.asList(fieldName)))
    );
    sourceQuerySpec.setNestedSpecs(Collections.singleton(targetQuerySpec));
    
    // Call findOne on the source resource repository and get the target resource by getting the
    // relation property.
    S sourceResource = sourceResourceRepository.findOne(sourceId, sourceQuerySpec);
    T targetResource = (T) PropertyUtils.getProperty(sourceResource, fieldName);
    
    if (targetResource == null) {
      throw new ResourceNotFoundException("Not Found");
    }
    
    return targetResource;
  }

  @Override
  public ResourceList<T> findManyTargets(Serializable sourceId, String fieldName,
      QuerySpec querySpec) {
    
    Class<?> sourceEntityClass = dtoJpaMapper.getEntityClassForDto(sourceResourceClass);
    
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
    Root<?> sourcePath = criteriaQuery.from(sourceEntityClass);
    
    Join<Object, Object> targetPath = sourcePath.join(fieldName);
    
    criteriaQuery.multiselect(this.selectionHandler.getSelections(querySpec, targetPath, resourceRegistry));
    
    List<Order> orders = querySpec.getSort().stream().map(sort -> {
      Function<Expression<?>, Order> orderFunc = sort.getDirection() == Direction.ASC ? cb::asc : cb::desc;
      return orderFunc.apply(this.selectionHandler.getExpression(targetPath, sort.getAttributePath()));
    })
    .collect(Collectors.toList());
    
    criteriaQuery.orderBy(orders);
    
    List<Predicate> restrictions = new ArrayList<>();
    
    restrictions.add(this.filterHandler.getRestriction(querySpec, cb, targetPath));
    
    restrictions.add(
        cb.equal(
            sourcePath.get(
                entityManager.getMetamodel()
                    .entity(sourceEntityClass)
                    .getId(Serializable.class)
            ),
            sourceId
        )
    );
    
    criteriaQuery.where(restrictions.stream().toArray(Predicate[]::new));
    
    List<Tuple> result = entityManager
        .createQuery(criteriaQuery)
        .setFirstResult(Optional.ofNullable(querySpec.getOffset()).orElse(Long.valueOf(0)).intValue())
        .setMaxResults(Optional.ofNullable(querySpec.getLimit()).orElse(Long.valueOf(100)).intValue())
        .getResultList();
    
    PagedMetaInformation metaInformation = new DefaultPagedMetaInformation();
    
    return new DefaultResourceList<>(
        result.stream()
        .map(JpaResourceRepository::mapFromTuple)
        .map(map -> this.mapper.map(map, this.targetResourceClass))
        .collect(Collectors.toList()),
        metaInformation,
        null
        );
  }

}
