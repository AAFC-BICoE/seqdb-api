package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.IncludeRelationSpec;

public abstract class BaseRepositoryTest extends BaseIntegrationTest {

  protected static final String TEST_PRIMER_NAME = "test primer";
  protected static final Integer TEST_PRIMER_LOT_NUMBER = 1;
  protected static final PrimerType TEST_PRIMER_TYPE = PrimerType.PRIMER;
  protected static final String TEST_PRIMER_SEQ = "test seq";
  
  protected static final String TEST_REGION_NAME = "test region";
  protected static final String TEST_REGION_DESCRIPTION = "test description";
  protected static final String TEST_REGION_SYMBOL = "test symbol";
  
  @Inject
  protected ResourceRegistry resourceRegistry;
  
  /**
   * By default, run as an admin user to avoid dealing with Group-based authorization for tests that
   * don't involve it.
   */
  @Before
  public void runAsAdminUser() {
    // Create the admin account.
    Account testAdminAccount = new Account();
    testAdminAccount.setAccountName("testAdminAccount");
    testAdminAccount.setAccountType("Admin");
    entityManager.persist(testAdminAccount);
    
    // Set the authentication in Spring's context.
    SecurityContextHolder.getContext().setAuthentication(
        new TestingAuthenticationToken(
            new User("testAdminAccount", "", Collections.emptyList()),
            ""
        )
    );
  }
  
  /**
   * Persists a PcrPrimer with the required fields set.
   *
   * @return the persisted primer
   */
  protected PcrPrimer persistTestPrimer() {
    PcrPrimer primer = new PcrPrimer();
    primer.setName(TEST_PRIMER_NAME);
    primer.setLotNumber(TEST_PRIMER_LOT_NUMBER);
    primer.setType(TEST_PRIMER_TYPE);
    primer.setSeq(TEST_PRIMER_SEQ);

    assertNull(primer.getId());
    entityManager.persist(primer);
    // New primer must have an ID.
    assertNotNull(primer.getId());

    return primer;
  }
  
  /**
   * Persists a test PcrPrimer with an associated Region.
   * 
   * @return the persisted primer
   */
  protected PcrPrimer persistTestPrimerWithRegion() {
    PcrPrimer primer = this.persistTestPrimer();
    
    Region region = new Region();
    region.setName(TEST_REGION_NAME);
    region.setDescription(TEST_REGION_DESCRIPTION);
    region.setSymbol(TEST_REGION_SYMBOL);
    
    assertNull(region.getId());
    entityManager.persist(region);
    assertNotNull(region.getId());
    
    primer.setRegion(region);
    
    return primer;
  }
  
  /**
   * Persists a test PCR batch with 22 reactions.
   * 
   * @return the persisted PCR batch.
   */
  protected PcrBatch persistTestPcrBatchWith22Reactions(String batchName) {
    PcrBatch batch = new PcrBatch();
    batch.setType(PcrBatchType.SANGER);
    batch.setName(batchName);
    batch.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
    
    for (int i = 1; i <= 22; i++) {
      PcrReaction reaction = new PcrReaction();
      reaction.setPcrName("reaction" + i);
      
      assertNull(reaction.getId());
      
      reaction.setPcrBatch(batch);
      batch.getReactions().add(reaction);
    }
    
    assertNull(batch.getId());
    entityManager.persist(batch);
    assertNotNull(batch.getId());
    
    assertEquals(22, batch.getReactions().size());
    batch.getReactions().forEach(BaseRepositoryTest::assertNotNull);
    
    return batch;
  };

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
