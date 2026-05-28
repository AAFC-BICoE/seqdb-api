package ca.gc.aafc.seqdb.api;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.toedter.spring.hateoas.jsonapi.JsonApiConfiguration;

import ca.gc.aafc.dina.DinaBaseApiAutoConfiguration;

@Configuration
@ComponentScan(basePackageClasses = DinaBaseApiAutoConfiguration.class)
@ImportAutoConfiguration(DinaBaseApiAutoConfiguration.class)
public class MainConfiguration {

  @Bean
  public JsonApiConfiguration jsonApiConfiguration() {
    return new JsonApiConfiguration()
      .withPluralizedTypeRendered(false)
      .withPageMetaAutomaticallyCreated(false)
      .withObjectMapperCustomizer(objectMapper -> {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JavaTimeModule());
      });
  }
}
