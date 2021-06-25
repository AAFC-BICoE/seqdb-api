package ca.gc.aafc.seqdb.api;

import java.util.function.Consumer;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ca.gc.aafc.dina.testsupport.DatabaseSupportService;
import ca.gc.aafc.dina.testsupport.PostgresTestContainerInitializer;
import ca.gc.aafc.seqdb.api.repository.ContainerTypeRepository;
import ca.gc.aafc.seqdb.api.repository.IndexSetRepository;
import ca.gc.aafc.seqdb.api.repository.LibraryPoolContentRepository;
import ca.gc.aafc.seqdb.api.repository.LibraryPoolRepository;
import ca.gc.aafc.seqdb.api.repository.LibraryPrepBatchRepository;
import ca.gc.aafc.seqdb.api.repository.LibraryPrepRepository;
import ca.gc.aafc.seqdb.api.repository.MolecularSampleRepository;
import ca.gc.aafc.seqdb.api.repository.NgsIndexRepository;
import ca.gc.aafc.seqdb.api.repository.PcrBatchRepository;
import ca.gc.aafc.seqdb.api.repository.PcrPrimerRepository;
import ca.gc.aafc.seqdb.api.repository.PreLibraryPrepRepository;
import ca.gc.aafc.seqdb.api.repository.ProductRepository;
import ca.gc.aafc.seqdb.api.repository.ProtocolRepository;
import ca.gc.aafc.seqdb.api.repository.ThermocyclerProfileRepository;

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
  
  // Should only be used with runInNewTransaction
  @Inject
  private EntityManagerFactory entityManagerFactory;

  @Inject
  protected DatabaseSupportService service;

  @Inject
  protected ContainerTypeRepository containerTypeRepository;

  @Inject
  protected IndexSetRepository indexSetRepository;

  @Inject
  protected LibraryPoolRepository libraryPoolRepository;

  @Inject
  protected LibraryPoolContentRepository libraryPoolContentRepository;

  @Inject
  protected LibraryPrepBatchRepository libraryPrepBatchRepository;

  @Inject
  protected LibraryPrepRepository libraryPrepRepository;

  @Inject
  protected MolecularSampleRepository molecularSampleRepository;

  @Inject
  protected ProductRepository productRepository;

  @Inject
  protected ProtocolRepository protocolRepository;

  @Inject
  protected NgsIndexRepository ngsIndexRepository;

  @Inject
  protected PreLibraryPrepRepository preLibraryPrepRepository;

  @Inject
  protected PcrPrimerRepository pcrPrimerRepository;

  @Inject
  protected ThermocyclerProfileRepository thermocyclerProfileRepository;

  @Inject
  protected PcrBatchRepository pcrBatchRepository;
  
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
