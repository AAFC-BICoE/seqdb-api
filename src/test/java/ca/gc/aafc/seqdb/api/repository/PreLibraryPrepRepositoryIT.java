package ca.gc.aafc.seqdb.api.repository;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.entities.PreLibraryPrep.PreLibraryPrepType;
import ca.gc.aafc.seqdb.factories.PreLibraryPrepFactory;

import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

@Ignore
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
  
  @Before
  public void setup() {
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

  @Test(expected = ResourceNotFoundException.class)
  public void deletePreLibraryPrep_onPreLibraryPrepNotFound_throwResourceNotFoundException() {
    preLibraryPrepRepository.delete(1000);
  } 
}
