package ca.gc.aafc.seqdb.api.repository.handlers;

import java.util.Map;

import javax.inject.Inject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class DtoJpaMapper {
  
  @NonNull
  private final Map<Class<?>, Class<?>> jpaEntities;
  
  public Class<?> getEntityClassForDto(Class<?> dtoClass) {
    return jpaEntities.get(dtoClass);
  }
  
}
