package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import com.google.common.collect.Iterables;

import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
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
 * JSONAPI repository that interfaces using DTOs, and uses JPA entities internally.
 *
 * @param <D> the JsonApi DTO class.
 */
@Transactional
@RequiredArgsConstructor
public class JpaResourceRepository<D>
    implements ResourceRepositoryV2<D, Serializable>, ResourceRegistryAware {

  /**
   * The JsonApi resource class.
   */
  @Getter
  private final Class<D> resourceClass;
  
  @NonNull
  private final JpaDtoRepository dtoRepository;

  @NonNull
  private final List<FilterHandler> filterHandlers;
  
  @Getter
  @Setter(onMethod_ = @Override)
  private ResourceRegistry resourceRegistry;

  @Override
  public D findOne(Serializable id, QuerySpec querySpec) {
    // Use the findAll method, but limit the result size to 1.
    querySpec.setLimit(Long.valueOf(1));
    ResourceList<D> resultSet = dtoRepository.findAll(
        this.resourceClass, querySpec, this.resourceRegistry,
        // Filter by ID.
        (root, query, cb) -> cb.equal(
            this.dtoRepository.getSelectionHandler()
                .getIdExpression(root, resourceClass, resourceRegistry),
            id
        ),
        // Not searching across a relationship, so no root change is required.
        null
    );
    
    // Throw the 404 exception if the resource is not found.
    if (resultSet.size() == 0) {
      throw new ResourceNotFoundException(
          this.resourceClass.getSimpleName() + " with ID " + id + " Not Found."
      );
    }
    
    // There should only be one result element in the list.
    return resultSet.get(0);
  }

  @Override
  public ResourceList<D> findAll(QuerySpec querySpec) {
    return this.findAll(null, querySpec);
  }

  @Override
  public ResourceList<D> findAll(@Nullable Iterable<Serializable> ids, QuerySpec querySpec) {
    
    return dtoRepository.findAll(
        this.resourceClass, querySpec, this.resourceRegistry,
        (root, query, cb) -> {
          List<Predicate> restrictions = new ArrayList<>();
          
          // Add the filter handler's restriction.
          for (FilterHandler filterHandler : this.filterHandlers) {
            restrictions.add(filterHandler.getRestriction(querySpec, root, query, cb));
          }
          
          // If the list of IDs is given, filter by ID.
          if (ids != null) {
            restrictions.add(
                this.dtoRepository.getSelectionHandler()
                    .getIdExpression(root, resourceClass, resourceRegistry)
                .in(Iterables.toArray(ids, Object.class))
            );
          }
          
          // Combine the restrictions in an 'and' operation.
          return cb.and(restrictions.stream().toArray(Predicate[]::new));
        },
        // Not searching across a relationship, so no root change is required.
        null
    );
    
  }

  @SuppressWarnings("unchecked")
  @Override
  public <S extends D> S save(S resource) {
    return (S) this.findOne(
        this.dtoRepository.save(resource, this.resourceRegistry),
        new QuerySpec(this.resourceClass)
    );
  }

  @SuppressWarnings("unchecked")
  @Override
  public <S extends D> S create(S resource) {
    return (S) this.findOne(
        this.dtoRepository.create(resource, this.resourceRegistry),
        new QuerySpec(this.resourceClass)
    );
  }

  @Override
  public void delete(Serializable id) {
    this.dtoRepository.delete(
        this.findOne(id, new QuerySpec(this.resourceClass)),
        this.resourceRegistry
    );
  }

}
