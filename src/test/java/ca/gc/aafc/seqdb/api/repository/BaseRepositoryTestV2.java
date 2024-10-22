package ca.gc.aafc.seqdb.api.repository;

import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;

@SpringBootTest(classes = SeqdbApiLauncher.class)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
public class BaseRepositoryTestV2 {

  @TestConfiguration
  public static class CollectionModuleTestConfigurationV2 {

    @Bean
    @ConditionalOnMissingBean
    public BuildProperties buildProperties() {
      Properties props = new Properties();
      props.setProperty("version", "agent-module-version");
      return new BuildProperties(props);
    }
  }
}
