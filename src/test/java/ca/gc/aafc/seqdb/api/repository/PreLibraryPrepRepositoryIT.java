package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep.PreLibraryPrepType;
import ca.gc.aafc.seqdb.api.testsupport.factories.PreLibraryPrepFactory;
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
  private BaseDAO baseDao;
  
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

  @BeforeEach
  public void setup() {
    createTestPreLibraryPrep();
  }
  
  @Test
  public void findPreLibraryPrep_whenNoFieldsAreSelected_preLibraryPrepReturnedWithAllFields() {
    PreLibraryPrepDto preLibraryPrepDto = preLibraryPrepRepository.findOne(
        testPreLibraryPrep.getUuid(), 
        new QuerySpec(PreLibraryPrepDto.class)
    );
    
    assertNotNull(preLibraryPrepDto);
    assertEquals(testPreLibraryPrep.getUuid(), preLibraryPrepDto.getUuid());
    assertEquals(TEST_PRELIBRARYPREP_TYPE, preLibraryPrepDto.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES, preLibraryPrepDto.getNotes());
    assertEquals(TEST_PRELIBRARYPREP_CONCENTRATION, preLibraryPrepDto.getConcentration());
  }
  
  @Test
  public void createPreLibraryPrep_onSuccess_allFieldsHaveSetValueAfterPersisted() {
    PreLibraryPrepDto newPreLibraryPrep = new PreLibraryPrepDto();
    newPreLibraryPrep.setPreLibraryPrepType(TEST_PRELIBRARYPREP_TYPE_CREATE);
    newPreLibraryPrep.setNotes(TEST_PRELIBRARYPREP_NOTES_CREATE);
    newPreLibraryPrep.setConcentration(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE);
    
    PreLibraryPrepDto createdPreLibraryPrep = preLibraryPrepRepository.create(newPreLibraryPrep);
    
    // DTO has the set value.
    assertNotNull(createdPreLibraryPrep.getUuid());
    assertEquals(TEST_PRELIBRARYPREP_TYPE_CREATE, createdPreLibraryPrep.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES_CREATE, createdPreLibraryPrep.getNotes());
    assertEquals(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE, createdPreLibraryPrep.getConcentration());
    
    // Entity has the set value
    PreLibraryPrep preLibraryPrepEntity = baseDao.findOneByNaturalId(createdPreLibraryPrep.getUuid(), PreLibraryPrep.class);
    assertNotNull(preLibraryPrepEntity.getUuid());
    assertEquals(TEST_PRELIBRARYPREP_TYPE_CREATE, preLibraryPrepEntity.getPreLibraryPrepType());
    assertEquals(TEST_PRELIBRARYPREP_NOTES_CREATE, preLibraryPrepEntity.getNotes());
    assertEquals(TEST_PRELIBRARYPREP_CONCENTRATION_CREATE, preLibraryPrepEntity.getConcentration());
  }
  
  @Test
  public void updatePreLibraryPrep_whenSomeFieldsAreUpdated_preLibraryPrepReturnedWithSelectedFieldsUpdated() {
   // Get the test prelibrarypreps's DTO.
   QuerySpec querySpec = new QuerySpec(PreLibraryPrepDto.class);

   PreLibraryPrepDto preLibraryPrepDto = preLibraryPrepRepository.findOne(
       testPreLibraryPrep.getUuid(), 
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
    preLibraryPrepRepository.delete(testPreLibraryPrep.getUuid());
    assertNull(entityManager.find(PreLibraryPrep.class, testPreLibraryPrep.getId()));
  }

  @Test
  public void deletePreLibraryPrep_onPreLibraryPrepNotFound_throwResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> preLibraryPrepRepository.delete(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );
  }
}
