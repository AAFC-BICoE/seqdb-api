package ca.gc.aafc.seqdb.api;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * 
 * Base class for integration tests working at the database-level.
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SeqdbApiLauncher.class)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
@ContextConfiguration(initializers = {PostgresTestContainerInitializer.class})
@Transactional
public abstract class BaseIntegrationTest {

  @PersistenceContext
  protected EntityManager entityManager;

}
