package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.IncludeRelationSpec;

public abstract class BaseRepositoryTest extends BaseIntegrationTest {
  
  @Inject
  protected ResourceRegistry resourceRegistry;

  @Inject
  private ApplicationContext appCtx;
  
  public static Reader newClasspathResourceReader(String classpathResourcePath) throws IOException {
    return new InputStreamReader(new ClassPathResource(classpathResourcePath).getInputStream(),
        StandardCharsets.UTF_8);
  }
  
  /**
   * Persists an entity.
   * 
   * @param the entity to persist
   */
  @SuppressWarnings("unchecked")
  protected <T extends DinaEntity> void persist(T objectToPersist) {
    // TODO add getId interface back? assertNull(objectToPersist.getId());

    // Get the DinaService for this entity:
    String[] serviceBeanNames = appCtx.getBeanNamesForType(
      ResolvableType.forClassWithGenerics(DinaService.class, objectToPersist.getClass())
    );
    String serviceBeanName = serviceBeanNames[0];
    DinaService<T> service = (DinaService<T>) appCtx.getBean(serviceBeanName);

    service.create(objectToPersist);
    // New primer must have an ID.
    // TODO add getId interface back? assertNotNull(objectToPersist.getId());
  }
  
  /**
   * Get a List<IncludeFieldSpec> from of an array of field names.
   * E.g. includeFieldSpecs("name", "description")
   * 
   * @param includedFields strings
   * @return List<IncludeFieldSpec>
   */
  protected static List<IncludeFieldSpec> includeFieldSpecs(String... includedFields) {
    return Arrays.asList(includedFields)
        .stream()
        .map(Arrays::asList)
        .map(IncludeFieldSpec::new)
        .collect(Collectors.toList());
  }
  
  /**
   * Get a List<IncludeRelationSpec> from an array of relation names.
   * E.g. includeRelationSpecs("region")
   * 
   * @param includedRelations strings
   * @return List<IncludeRelationSpec>
   */
  protected static List<IncludeRelationSpec> includeRelationSpecs(String... includedRelations) {
    return Arrays.asList(includedRelations)
        .stream()
        .map(Arrays::asList)
        .map(IncludeRelationSpec::new)
        .collect(Collectors.toList());
  }
  
}
