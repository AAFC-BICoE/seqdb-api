package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import ca.gc.aafc.seqdb.api.repository.handlers.DtoJpaMapper;
import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
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
    From<?, ?>[] sourcePathHolder = new From<?,?>[1];
    
    @SuppressWarnings("unchecked")
    ResourceList<T> resultSet = (ResourceList<T>) dtoRepository.findAll(sourceResourceClass, querySpec, resourceRegistry,
        // Add the filters to the target entity's path.
        (targetPath, cb) -> {
          From<?,?> sourcePath = sourcePathHolder[0];
          
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

}
