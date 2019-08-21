package ca.gc.aafc.seqdb.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SeqdbApiLauncher.class)
@Transactional
public abstract class BaseIntegrationTest {

  @PersistenceContext
  protected EntityManager entityManager;

}
