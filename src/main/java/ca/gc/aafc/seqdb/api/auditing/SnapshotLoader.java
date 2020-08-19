package ca.gc.aafc.seqdb.api.auditing;

import java.util.Map;
import java.util.function.Function;

import javax.inject.Named;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.dina.mapper.JpaDtoMapper;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.entities.Region;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.QuerySpec;
import lombok.RequiredArgsConstructor;

/**
 * Provides entity-specific loading of audit snapshots.
 * Auditable DTOs must have snapshot loaders defined in this class.
 */
@Named
@RequiredArgsConstructor
public class SnapshotLoader {

  private final JpaDtoMapper jpaDtoMapper;
  private final ResourceRegistry resourceRegistry;

  /** Map from entity class to snapshot loader function. */
  private final Map<Class<?>, Function<Object, Object>> loaders = ImmutableMap.<Class<?>, Function<Object, Object>>builder()
      .put(Region.class, this::loadRegionSnapshot)  
      .build();

  public boolean supports(Class<?> entityClass) {
    return this.loaders.containsKey(entityClass);
  }
      
  public Object loadSnapshot(Object entity) {
    Function<Object, Object> loader = this.loaders.get(entity.getClass());
    return loader.apply(entity);
  }

  private Object loadRegionSnapshot(Object entity) {
    QuerySpec querySpec = new QuerySpec(Region.class);      
    return (RegionDto) jpaDtoMapper
        .toDto(entity, querySpec, resourceRegistry);
  } 
}
