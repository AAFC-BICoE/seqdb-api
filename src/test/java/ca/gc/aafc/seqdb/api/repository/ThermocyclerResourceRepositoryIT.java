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
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.ThermocycleProfile;
import ca.gc.aafc.seqdb.api.testsupport.factories.ThermocyclerProfileFactory;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class ThermocyclerResourceRepositoryIT extends BaseRepositoryTest {
  
  
  private static final String TEST_PROFILE_NAME = "test name";
  
  private static final String TEST_PROFILE_CYCLE = "Cycle";

  @Inject
  private BaseDAO baseDao;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;
  
  private ThermocycleProfile testThermocycleProfile;
  
  private ThermocycleProfile createTestProfile() {
    testThermocycleProfile = ThermocyclerProfileFactory.newThermocycleProfile()
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
  persist(testThermocycleProfile);
    
  return testThermocycleProfile;
  }
  
  private void verifyStepsAreEqual(ThermocycleProfile entity, ThermocyclerProfileDto dto) {
    
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
  
  @BeforeEach
  public void setup() {
    createTestProfile();
  }
  
  @Test
  public void findThermocyclerProfile_whenNoFieldsAreSelected_productReturnedWithAllFields() {
    ThermocyclerProfileDto thermoDto = thermocyclerProfileRepository.findOne(testThermocycleProfile.getUuid(), new QuerySpec(ThermocyclerProfileDto.class));
    assertNotNull(thermoDto);
    assertEquals(testThermocycleProfile.getUuid(), thermoDto.getUuid());
    assertEquals(TEST_PROFILE_NAME, thermoDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, thermoDto.getCycles());
    verifyStepsAreEqual(testThermocycleProfile, thermoDto);

  }
  
  @Test
  public void createAndPersistThermocycleProfile_onSucess_allFieldsHavePersistedValues() {
    String newThermocyclerProfileName = "new thermocycler profile";

    //set a base DTO
    ThermocyclerProfileDto baseDto = new ThermocyclerProfileDto();
    baseDto.setName(newThermocyclerProfileName);
    baseDto.setCycles(TEST_PROFILE_CYCLE);
    
    //create the DTO in the repository
    ThermocyclerProfileDto createdDto = thermocyclerProfileRepository.create(baseDto);
    
    //Assert DTO has the set values
    assertNotNull(createdDto.getUuid());
    assertEquals(newThermocyclerProfileName, createdDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, createdDto.getCycles());
    verifyStepsAreEqual(baseDto, createdDto);
    
    //Assert Entity has the set values
    ThermocycleProfile profileEntity = baseDao.findOneByNaturalId(createdDto.getUuid(), ThermocycleProfile.class);
    assertNotNull(profileEntity.getId());
    assertEquals(newThermocyclerProfileName, profileEntity.getName());
    assertEquals(TEST_PROFILE_CYCLE, profileEntity.getCycles());
    verifyStepsAreEqual(profileEntity, baseDto);
      
    
  }
  
  @Test
  public void updateThermocycleProfile_dtoWithOnlyUpdatedFields_entityReturnedWithUpdatedFields() {
    
    QuerySpec querySpec = new QuerySpec(ThermocyclerProfileDto.class);
    
    ThermocyclerProfileDto thermoDto = thermocyclerProfileRepository.findOne(testThermocycleProfile.getUuid(), querySpec);
    
    thermoDto.setCycles("new cycles");
    
    thermocyclerProfileRepository.save(thermoDto);
    
    assertEquals("new cycles", testThermocycleProfile.getCycles());
    
  }

  @Test
  public void deleteThermocycleProfile_callRepositoryDeleteOnID_profileNotFound() {
    thermocyclerProfileRepository.delete(testThermocycleProfile.getUuid());
    assertNull(entityManager.find(ThermocycleProfile.class, testThermocycleProfile.getId()));
    
  }

  @Test
  public void deleteThermocycleProfile_nonexistentID_throwsResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> thermocyclerProfileRepository.delete(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );
  }

}
