package ca.gc.aafc.seqdb.api.repository;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.entities.PcrProfile;
import ca.gc.aafc.seqdb.factories.PcrProfileFactory;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class ThermocyclerResourceRepositoryIT extends BaseRepositoryTest {
  
  
  private static final String TEST_PROFILE_NAME = "test name";
  
  private static final String TEST_PROFILE_CYCLE = "Cycle";
  
  @Inject
  private ResourceRepositoryV2<ThermocyclerProfileDto, Serializable> thermoRepository;
  
  
  private PcrProfile testPcrProfile;
  
  private PcrProfile createTestProfile() {
    testPcrProfile = PcrProfileFactory.newPcrProfile()
        .name(TEST_PROFILE_NAME)
        .cycles(TEST_PROFILE_CYCLE)
        .step1("1")
        .step2("2")
        .step3("3")
        .step4("4")
        .step5("5")
        .step6("6")
        .step7("7")
        .step8("8")
        .step9("9")
        .step10("10")
        .step11("11")
        .step12("12")
        .step13("13")
        .step14("14")
        .step15("15")
        .build();
  persist(testPcrProfile);
    
  return testPcrProfile;
  }
  
  private void verifyStepsAreEqual(PcrProfile entity, ThermocyclerProfileDto dto) {
    
    assertEquals(dto.getStep1(), entity.getStep1());
    assertEquals(dto.getStep2(), entity.getStep2());
    assertEquals(dto.getStep3(), entity.getStep3());
    assertEquals(dto.getStep4(), entity.getStep4());
    assertEquals(dto.getStep5(), entity.getStep5());
    assertEquals(dto.getStep6(), entity.getStep6());
    assertEquals(dto.getStep7(), entity.getStep7());
    assertEquals(dto.getStep8(), entity.getStep8());
    assertEquals(dto.getStep9(), entity.getStep9());
    assertEquals(dto.getStep10(), entity.getStep10());
    assertEquals(dto.getStep11(), entity.getStep11());
    assertEquals(dto.getStep12(), entity.getStep12());
    assertEquals(dto.getStep13(), entity.getStep13());
    assertEquals(dto.getStep14(), entity.getStep14());
    assertEquals(dto.getStep15(), entity.getStep15());

  }
  
  private void verifyStepsAreEqual(ThermocyclerProfileDto baseDto, ThermocyclerProfileDto newDto) {
    
    assertEquals(newDto.getStep1(), baseDto.getStep1());
    assertEquals(newDto.getStep2(), baseDto.getStep2());
    assertEquals(newDto.getStep3(), baseDto.getStep3());
    assertEquals(newDto.getStep4(), baseDto.getStep4());
    assertEquals(newDto.getStep5(), baseDto.getStep5());
    assertEquals(newDto.getStep6(), baseDto.getStep6());
    assertEquals(newDto.getStep7(), baseDto.getStep7());
    assertEquals(newDto.getStep8(), baseDto.getStep8());
    assertEquals(newDto.getStep9(), baseDto.getStep9());
    assertEquals(newDto.getStep10(), baseDto.getStep10());
    assertEquals(newDto.getStep11(), baseDto.getStep11());
    assertEquals(newDto.getStep12(), baseDto.getStep12());
    assertEquals(newDto.getStep13(), baseDto.getStep13());
    assertEquals(newDto.getStep14(), baseDto.getStep14());
    assertEquals(newDto.getStep15(), baseDto.getStep15());

  }
  
  @Before
  public void setup() {
    createTestProfile();
  }
  
  @Test
  public void findThermocyclerProfile_whenNoFieldsAreSelected_productReturnedWithAllFields() {
    ThermocyclerProfileDto thermoDto = thermoRepository.findOne(testPcrProfile.getPcrProfileId(), new QuerySpec(ThermocyclerProfileDto.class));
    assertNotNull(thermoDto);
    assertEquals(testPcrProfile.getId(), thermoDto.getPcrProfileId());
    assertEquals(TEST_PROFILE_NAME, thermoDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, thermoDto.getCycles());
    verifyStepsAreEqual(testPcrProfile, thermoDto);

  }
  
  @Test
  public void findThermocyclerProfile_whenFieldsAreSelected_pcrProfileReturnedWithSelectedFieldsOnly() {
    QuerySpec querySpec = new QuerySpec(ThermocyclerProfileDto.class);
    querySpec.setIncludedFields(includeFieldSpecs("name", "step1"));
    
    ThermocyclerProfileDto thermoDto = thermoRepository.findOne(testPcrProfile.getPcrProfileId(), querySpec);
    assertNotNull(thermoDto);
    assertEquals(TEST_PROFILE_NAME, thermoDto.getName());
    assertNull(thermoDto.getCycles());
    assertEquals("1", thermoDto.getStep1());
    assertNull(thermoDto.getStep2());
    assertNull(thermoDto.getStep10());
  }
  
  @Test
  public void createAndPersistPcrProfile_onSucess_allFieldsHavePersistedValues() {
    //set a base DTO
    ThermocyclerProfileDto baseDto = new ThermocyclerProfileDto();
    baseDto.setName(TEST_PROFILE_NAME);
    baseDto.setCycles(TEST_PROFILE_CYCLE);
    
    //create the DTO in the repository
    ThermocyclerProfileDto createdDto = thermoRepository.create(baseDto);
    
    //Assert DTO has the set values
    assertNotNull(createdDto.getPcrProfileId());
    assertEquals(TEST_PROFILE_NAME, createdDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, createdDto.getCycles());
    verifyStepsAreEqual(baseDto, createdDto);
    
    //Assert Entity has the set values
    PcrProfile profileEntity = entityManager.find(PcrProfile.class, createdDto.getPcrProfileId());
    assertNotNull(profileEntity.getId());
    assertEquals(TEST_PROFILE_NAME, profileEntity.getName());
    assertEquals(TEST_PROFILE_CYCLE, profileEntity.getCycles());
    verifyStepsAreEqual(profileEntity, baseDto);
      
    
  }
  
  @Test
  public void updatePcrProfile_dtoWithOnlyUpdatedFields_entityReturnedWithUpdatedFields() {
    
    QuerySpec querySpec = new QuerySpec(ThermocyclerProfileDto.class);
    
    ThermocyclerProfileDto thermoDto = thermoRepository.findOne(testPcrProfile.getId(), querySpec);
    
    thermoDto.setCycles("new cycles");
    
    thermoRepository.save(thermoDto);
    
    assertEquals("new cycles", testPcrProfile.getCycles());
    
  }

  @Test
  public void deletePcrProfile_callRepositoryDeleteOnID_profileNotFound() {
    thermoRepository.delete(testPcrProfile.getId());
    assertNull(entityManager.find(PcrProfile.class, testPcrProfile.getId()));
    
  }
  
  @Test(expected = ResourceNotFoundException.class)
  public void deletePcrProfile_nonexistentID_throwsResourceNotFoundException() {
    thermoRepository.delete(42);
  }
  
  @Test
  public void listThermocyclerProfile_APIResponse_schemaValidates() throws IOException {
    JsonSchemaAssertions.assertJsonSchema(
        BaseRepositoryTest.newClasspathResourceReader("static/json-schema/GETthermocyclerJSONSchema.json"),
        BaseRepositoryTest.newClasspathResourceReader("realThermoResponse-all.json"));
  }

  @Test
  public void getThermocyclerProfile_APIResponse_schemaValidates() throws IOException {
    JsonSchemaAssertions.assertJsonSchema(
        BaseRepositoryTest.newClasspathResourceReader("static/json-schema/thermocyclerJSONSchema.json"),
        BaseRepositoryTest.newClasspathResourceReader("realThermoResponse.json"));
  } 
}
