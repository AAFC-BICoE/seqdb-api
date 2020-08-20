package ca.gc.aafc.seqdb.api.auditing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import com.google.common.collect.ImmutableMap;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.mapper.JpaDtoMapper;
import ca.gc.aafc.dina.util.ClassAnnotationHelper;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
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
  private Map<Class<?>, BiFunction<Object, Class<?>, Object>> loaders = null;
  
  @PostConstruct
  private void postConstruct() {
    Set<Class<?>> dtoClassList = ClassAnnotationHelper.findAnnotatedClasses(RegionDto.class, RelatedEntity.class);
    Map<Class<?>, BiFunction<Object, Class<?>,Object>> loader = new HashMap<Class<?>, BiFunction<Object, Class<?>,Object>>();
    for( Class<?> dtoClass: dtoClassList) {
      loader.put((Class<?>)jpaDtoMapper.getEntityClassForDto(dtoClass), this::loadEntitySnapshot);
    }        
    loaders = ImmutableMap.<Class<?>, BiFunction<Object, Class<?>,Object>>builder()
         .putAll(loader)
         .build();  
  }    
  

  public boolean supports(Class<?> entityClass) {
    return this.loaders.containsKey(entityClass);
  }
      
  public Object loadSnapshot(Object entity) {
    BiFunction<Object,Class<?>,  Object> loader = this.loaders.get(entity.getClass());
    return loader.apply(entity, jpaDtoMapper.getDtoClassForEntity(entity.getClass()));
  }

 
  private Object loadEntitySnapshot(Object entity, Class<?> dtoClass) {
    QuerySpec querySpec = new QuerySpec(dtoClass);      
    return  jpaDtoMapper
        .toDto(entity, querySpec, resourceRegistry);
  }   
}
