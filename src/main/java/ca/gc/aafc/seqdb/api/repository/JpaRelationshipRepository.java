package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

import io.crnk.core.engine.internal.utils.PropertyUtils;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.engine.registry.ResourceRegistryAware;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.RelationshipRepositoryV2;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
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
    throw new UnsupportedOperationException();
  }

}
