package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.Group;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.interfaces.UniqueObj;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.IncludeRelationSpec;

public abstract class BaseRepositoryTest extends BaseIntegrationTest {
  
  @Inject
  protected ResourceRegistry resourceRegistry;
  
  
  public static Reader newClasspathResourceReader(String classpathResourcePath) throws IOException {
    return new InputStreamReader(new ClassPathResource(classpathResourcePath).getInputStream(),
        StandardCharsets.UTF_8);
  }
  
  /**
   * By default, run as an admin user to avoid dealing with Group-based authorization for tests that
   * don't involve it.
   */
  @BeforeEach
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
   * Persists an entity.
   * 
   * @param the entity to persist
   */
  protected void persist(UniqueObj objectToPersist) {
    assertNull(objectToPersist.getId());
    entityManager.persist(objectToPersist);
    // New primer must have an ID.
    assertNotNull(objectToPersist.getId());
  }
  
  /**
   * Persists a group
   * 
   * @param the group to persist
   */
  protected void persistGroup(Group groupToPersist) {
    assertNull(groupToPersist.getGroupId());
    entityManager.persist(groupToPersist);
    assertNotNull(groupToPersist.getGroupId());
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
      reaction.setTubeNumber(i);
      
      assertNull(reaction.getId());
      
      reaction.setPcrBatch(batch);
      batch.getReactions().add(reaction);
    }
    
    assertNull(batch.getId());
    entityManager.persist(batch);
    assertNotNull(batch.getId());
    
    assertEquals(22, batch.getReactions().size());
    batch.getReactions().forEach(Assertions::assertNotNull);
    
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
