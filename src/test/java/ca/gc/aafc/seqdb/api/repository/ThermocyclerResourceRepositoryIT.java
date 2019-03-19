package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.HashMap;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.entities.PcrProfile;
import ca.gc.aafc.seqdb.factories.PcrProfileFactory;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class ThermocyclerResourceRepositoryIT extends BaseRepositoryTest {
  
  
  private static final String TEST_PROFILE_NAME = "test name";
  
  private static final String TEST_PROFILE_CYCLE = "Cycle";
  
  private static final HashMap<Integer, String> EXPECTED_MAP = new HashMap<Integer, String>() {

    private static final long serialVersionUID = 7044890286044881851L;

    {
      put(1, "1");
      put(2, "2");
      put(3, "3");
      put(4, "4");
      put(5, "5");
      put(6, "6");
      put(7, "7");
      put(8, "8");
      put(9, "9");
      put(10, "10");
      put(11, "11");
      put(12, "12");
      put(13, "13");
      put(14, "14");
      put(15, "15");
    }
  };
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
  
  private void verifyPcrProfileSteps(PcrProfile pcr) {
    
    assertEquals("1", pcr.getStep1());
    assertEquals("2", pcr.getStep2());
    assertEquals("3", pcr.getStep3());
    assertEquals("4", pcr.getStep4());
    assertEquals("5", pcr.getStep5());
    assertEquals("6", pcr.getStep6());
    assertEquals("7", pcr.getStep7());
    assertEquals("8", pcr.getStep8());
    assertEquals("9", pcr.getStep9());
    assertEquals("10", pcr.getStep10());
    assertEquals("11", pcr.getStep11());
    assertEquals("12", pcr.getStep12());
    assertEquals("13", pcr.getStep13());
    assertEquals("14", pcr.getStep14());
    assertEquals("15", pcr.getStep15());

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
    assertEquals(EXPECTED_MAP, thermoDto.getSteps());

  }
  
  @Test
  public void findThermocyclerProfile_whenFieldsAreSelected_pcrProfileReturnedWithSelectedFieldsOnly() {
    QuerySpec querySpec = new QuerySpec(ThermocyclerProfileDto.class);
    querySpec.setIncludedFields(includeFieldSpecs("name", "steps"));
    
    ThermocyclerProfileDto thermoDto = thermoRepository.findOne(testPcrProfile.getPcrProfileId(), querySpec);
    assertNotNull(thermoDto);
    assertEquals(TEST_PROFILE_NAME, thermoDto.getName());
    assertEquals(EXPECTED_MAP, thermoDto.getSteps());
    assertNull(thermoDto.getCycles());
  }
  
  @Test
  public void createPcrProfile_onSucess_allFieldsHavePersistedValues() {
    //set a base DTO
    ThermocyclerProfileDto baseDto = new ThermocyclerProfileDto();
    baseDto.setName(TEST_PROFILE_NAME);
    baseDto.setCycles(TEST_PROFILE_CYCLE);
    baseDto.setSteps(EXPECTED_MAP);
    
    //create the DTO in the repository
    ThermocyclerProfileDto createdDto = thermoRepository.create(baseDto);
    
    //Assert DTO has the set values
    assertNotNull(createdDto.getPcrProfileId());
    assertEquals(TEST_PROFILE_NAME, createdDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, createdDto.getCycles());
    assertEquals(EXPECTED_MAP, createdDto.getSteps());
    
    //Assert Entity has the set values
    PcrProfile profileEntity = entityManager.find(PcrProfile.class, createdDto.getPcrProfileId());
    assertNotNull(profileEntity.getId());
    assertEquals(TEST_PROFILE_NAME, profileEntity.getName());
    assertEquals(TEST_PROFILE_CYCLE, profileEntity.getCycles());
    verifyPcrProfileSteps(profileEntity);
    
    
  }

}
