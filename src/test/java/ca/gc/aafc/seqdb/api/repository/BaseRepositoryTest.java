package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.inject.Inject;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;

import ca.gc.aafc.dina.entity.DinaEntity;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
public abstract class BaseRepositoryTest extends BaseIntegrationTest {

  @Inject
  private ApplicationContext appCtx;
  
  public static Reader newClasspathResourceReader(String classpathResourcePath) throws IOException {
    return new InputStreamReader(new ClassPathResource(classpathResourcePath).getInputStream(),
        StandardCharsets.UTF_8);
  }

  @TestConfiguration
  public static class CollectionModuleTestConfiguration {
    @Bean
    public BuildProperties buildProperties() {
      Properties props = new Properties();
      props.setProperty("version", "agent-module-version");
      return new BuildProperties(props);
    }
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
  
}
