package ca.gc.aafc.seqdb.api;

import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * Base class for integration tests working at the database-level.
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SeqdbApiLauncher.class)
@Transactional
public abstract class BaseIntegrationTest {

  @PersistenceContext
  protected EntityManager entityManager;
  
  // Should only be used with runInNewTransaction
  @Autowired
  private EntityManagerFactory entityManagerFactory;
  
  /**
   * Accepts a {@link Consumer} of {@link EntityManager} that will be called in a new, unmanaged transaction.
   * The main goal is to not interfere with SpringTest Managed transaction.
   * Note that the Transaction will be committed.
   * 
   * This should only be used for setup/tear down purpose.
   * 
   * @param emConsumer
   */
  protected void runInNewTransaction(Consumer<EntityManager> emConsumer) {
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction et = em.getTransaction();
    et.begin();
    emConsumer.accept(em);
    em.flush();
    et.commit();
    em.close();
  }

}
