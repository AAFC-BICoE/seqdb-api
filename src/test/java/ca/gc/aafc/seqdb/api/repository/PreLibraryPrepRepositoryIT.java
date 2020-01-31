package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.entities.PreLibraryPrep.PreLibraryPrepType;
import ca.gc.aafc.seqdb.testsupport.factories.PreLibraryPrepFactory;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

/**
 * Integration test for PreLibraryPrep Repository.
 * 
 * This test is designed to work only using Postgresql, if you are using any other database to run
 * the test, the tests will be ignored.
 */
public class PreLibraryPrepRepositoryIT extends BaseRepositoryTest {

  protected static final PreLibraryPrepType TEST_PRELIBRARYPREP_TYPE = PreLibraryPrepType.SHEARING;
  protected static final String TEST_PRELIBRARYPREP_NOTES = "PreLibraryPrep notes";
  protected static final Double TEST_PRELIBRARYPREP_CONCENTRATION = 5.3;
  
  protected static final PreLibraryPrepType TEST_PRELIBRARYPREP_TYPE_CREATE = PreLibraryPrepType.SIZE_SELECTION;
  protected static final String TEST_PRELIBRARYPREP_NOTES_CREATE = "PreLibraryPrep notes create";
  protected static final Double TEST_PRELIBRARYPREP_CONCENTRATION_CREATE = 3.2;
  
  protected static final String TEST_PRELIBRARYPREP_NOTES_UPDATE = "PreLibraryPrep notes update";
  
  @Inject
  private PreLibraryPrepRepository preLibraryPrepRepository;
  
  @Inject
  private DataSource datasource;
  
  private PreLibraryPrep testPreLibraryPrep;
  
  private PreLibraryPrep createTestPreLibraryPrep() {
    testPreLibraryPrep = PreLibraryPrepFactory.newPreLibraryPrep()
        .preLibraryPrepType(TEST_PRELIBRARYPREP_TYPE)
        .notes(TEST_PRELIBRARYPREP_NOTES)
        .concentration(TEST_PRELIBRARYPREP_CONCENTRATION)
        .build();
    
    persist(testPreLibraryPrep);
    return testPreLibraryPrep;
  }

  /**
   * Since H2 does not support creating enum types, the integration tests will only work using
   * postgresql. If you are using another dbms it will ignore all of the tests.
   */
  @BeforeEach
  public void setup() {
    // Assume that the tests are running only on postgresql, if not the tests will
    // be ignored.
    try {
      assumeTrue(
          "PostgreSQL".equals(datasource.getConnection().getMetaData().getDatabaseProductName()),
          "PreLibraryPrep tests were ignored since the test environment is not running on PostgreSQL.");
    } catch (SQLException e) {
      fail("Datasource could not be found. Make sure your connection is setup properly.");
    }

    createTestPreLibraryPrep();
  }
  
  @Test
  public void findPreLibraryPrep_whenNoFieldsAreSelected_preLibraryPrepReturnedWithAllFields() {
    PreLibraryPrepDto preLibraryPrepDto = preLibraryPrepRepository.findOne(
        testPreLibraryPrep.getId(), 
        new QuerySpec(PreLibraryPrepDto.class)
    );
    
    assertNotNull(preLibraryPrepDto);
    assertEquals(testPreLibraryPrep.getId(), preLibraryPrepDto.getPreLibraryPrepId());
    assertEquals(TEST_PRELIBRARYPREP_TYPE, preLibraryPrepDto.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES, preLibraryPrepDto.getNotes());
    assertEquals(TEST_PRELIBRARYPREP_CONCENTRATION, preLibraryPrepDto.getConcentration());
  }
  
  @Test
  public void findPreLibraryPrep_whenFieldsAreSelected_preLibraryPrepReturnedWithSelectedFieldsOnly() {
    QuerySpec querySpec = new QuerySpec(PreLibraryPrepDto.class);
    querySpec.setIncludedFields(includeFieldSpecs("preLibraryPrepType", "notes"));
    
    PreLibraryPrepDto preLibraryPrepDto = preLibraryPrepRepository.findOne(
        testPreLibraryPrep.getId(), 
        querySpec
    );
    
    assertNotNull(preLibraryPrepDto);
    assertEquals(testPreLibraryPrep.getId(), preLibraryPrepDto.getPreLibraryPrepId());
    assertEquals(TEST_PRELIBRARYPREP_TYPE, preLibraryPrepDto.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES, preLibraryPrepDto.getNotes());
    assertNull(preLibraryPrepDto.getConcentration());
  }
  
  @Test
  public void createPreLibraryPrep_onSuccess_allFieldsHaveSetValueAfterPersisted() {
    PreLibraryPrepDto newPreLibraryPrep = new PreLibraryPrepDto();
    newPreLibraryPrep.setPreLibraryPrepType(TEST_PRELIBRARYPREP_TYPE_CREATE);
    newPreLibraryPrep.setNotes(TEST_PRELIBRARYPREP_NOTES_CREATE);
    newPreLibraryPrep.setConcentration(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE);
    
    PreLibraryPrepDto createdPreLibraryPrep = preLibraryPrepRepository.create(newPreLibraryPrep);
    
    // DTO has the set value.
    assertNotNull(createdPreLibraryPrep.getPreLibraryPrepId());
    assertEquals(TEST_PRELIBRARYPREP_TYPE_CREATE, createdPreLibraryPrep.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES_CREATE, createdPreLibraryPrep.getNotes());
    assertEquals(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE, createdPreLibraryPrep.getConcentration());
    
    // Entity has the set value
    PreLibraryPrep preLibraryPrepEntity = entityManager.find(PreLibraryPrep.class, createdPreLibraryPrep.getPreLibraryPrepId());
    assertNotNull(preLibraryPrepEntity.getId());
    assertEquals(TEST_PRELIBRARYPREP_TYPE_CREATE, preLibraryPrepEntity.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES_CREATE, preLibraryPrepEntity.getNotes());
    assertEquals(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE, preLibraryPrepEntity.getConcentration());
  }
  
  @Test
  public void updatePreLibraryPrep_whenSomeFieldsAreUpdated_preLibraryPrepReturnedWithSelectedFieldsUpdated() {
   // Get the test prelibrarypreps's DTO.
   QuerySpec querySpec = new QuerySpec(PreLibraryPrepDto.class);

   PreLibraryPrepDto preLibraryPrepDto = preLibraryPrepRepository.findOne(
       testPreLibraryPrep.getId(), 
       querySpec
   );
   
   // Change the DTO's notes value.
   preLibraryPrepDto.setNotes(TEST_PRELIBRARYPREP_NOTES_UPDATE);
   
   // Save the DTO using the repository.
   preLibraryPrepRepository.save(preLibraryPrepDto);
   
   // Check that the entity has the new notes value.
   assertEquals(TEST_PRELIBRARYPREP_NOTES_UPDATE, testPreLibraryPrep.getNotes());
  }
  
  @Test
  public void deletePreLibraryPrep_onPreLibraryPrepLookup_preLibraryPrepNotFound() {
    preLibraryPrepRepository.delete(testPreLibraryPrep.getId());
    assertNull(entityManager.find(PreLibraryPrep.class, testPreLibraryPrep.getId()));
  }

  @Test
  public void deletePreLibraryPrep_onPreLibraryPrepNotFound_throwResourceNotFoundException() {
    assertThrows(ResourceNotFoundException.class, () -> preLibraryPrepRepository.delete(1000));
  }
}
