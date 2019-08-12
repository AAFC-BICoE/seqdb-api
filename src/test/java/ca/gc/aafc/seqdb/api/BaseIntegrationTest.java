package ca.gc.aafc.seqdb.api;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SeqdbApiLauncher.class, 
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "import-sample-accounts=true", 
    "seqdb.trusted-service-api-keys = test-api-key, another-test-api-key"
})
@Transactional
public abstract class BaseIntegrationTest extends TestCase {

  @LocalServerPort
  protected int testPort;
  
  @PersistenceContext
  protected EntityManager entityManager;

}
