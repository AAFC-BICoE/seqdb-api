package ca.gc.aafc.seqdb.api.repository;

import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.dto.JsonApiResource;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.dina.repository.DinaRepositoryV2;
import ca.gc.aafc.dina.repository.JsonApiModelAssistant;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.dina.testsupport.jsonapi.JsonAPITestHelper;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;

@SpringBootTest(classes = SeqdbApiLauncher.class)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class BaseRepositoryTestV2 {

  protected UUID createWithRepository(JsonApiResource dto, DinaRepositoryV2<?, ?> repo) {
    JsonApiDocument toCreate = JsonApiDocuments.createJsonApiDocument(
      null, dto.getJsonApiType(),
      JsonAPITestHelper.toAttributeMap(dto)
    );

    return JsonApiModelAssistant.extractUUIDFromRepresentationModelLink(repo
      .handleCreate(toCreate, null));
  }

  protected UUID createWithRepository(JsonApiResource dto, Function<JsonApiDocument, ResponseEntity<RepresentationModel<?>>> onCreateMethod) {
    JsonApiDocument docToCreate = JsonApiDocuments.createJsonApiDocument(
      null, dto.getJsonApiType(),
      JsonAPITestHelper.toAttributeMap(dto)
    );
    return JsonApiModelAssistant.extractUUIDFromRepresentationModelLink(onCreateMethod.apply(docToCreate));
  }

  @TestConfiguration
  public static class CollectionModuleTestConfigurationV2 {

    @Bean
    @ConditionalOnMissingBean
    public BuildProperties buildProperties() {
      Properties props = new Properties();
      props.setProperty("version", "seqdb-api-module-version");
      return new BuildProperties(props);
    }
  }
}
