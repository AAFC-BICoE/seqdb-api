package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;

import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.RelationshipRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaRelationshipRepository<T, D> implements RelationshipRepositoryV2<T, Serializable, D, Serializable> {

  @NonNull
  @Getter
  private final Class<T> sourceResourceClass;
  
  @NonNull
  @Getter
  private final Class<D> targetResourceClass;
  
  @Override
  public void setRelation(T source, Serializable targetId, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRelations(T source, Iterable<Serializable> targetIds, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addRelations(T source, Iterable<Serializable> targetIds, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeRelations(T source, Iterable<Serializable> targetIds, String fieldName) {
    throw new UnsupportedOperationException();
  }

  @Override
  public D findOneTarget(Serializable sourceId, String fieldName, QuerySpec querySpec) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResourceList<D> findManyTargets(Serializable sourceId, String fieldName,
      QuerySpec querySpec) {
    throw new UnsupportedOperationException();
  }

}
