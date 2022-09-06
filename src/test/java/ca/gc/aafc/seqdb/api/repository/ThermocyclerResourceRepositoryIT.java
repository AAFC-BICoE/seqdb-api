package ca.gc.aafc.seqdb.api.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import ca.gc.aafc.seqdb.api.service.ThermocyclerProfileService;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.ThermocyclerProfileTestFixture;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import ca.gc.aafc.seqdb.api.testsupport.factories.ThermocyclerProfileFactory;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class ThermocyclerResourceRepositoryIT extends BaseRepositoryTest {
  
  private static final String TEST_PROFILE_NAME = "test name";
  private static final String TEST_PROFILE_CYCLE = ThermocyclerProfileTestFixture.TEST_PROFILE_CYCLE;

  @Inject
  private ThermocyclerProfileService thermocyclerProfileService;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;


  private static ThermocyclerProfile.ThermocyclerProfileBuilder initTestProfileBuilder() {
    return ThermocyclerProfileFactory.newThermocyclerProfile()
            .name(TEST_PROFILE_NAME)
            .cycles(TEST_PROFILE_CYCLE)
            .steps(List.of("step1", "step2"));
  }

  private ThermocyclerProfile createTestProfile() {
    ThermocyclerProfile testThermocyclerProfile = initTestProfileBuilder()
            .build();
    return thermocyclerProfileService.create(testThermocyclerProfile);
  }

  @Test
  public void createThermocyclerProfile_whenStepsSizeExceeded_ExceptionThrown() {
    ThermocyclerProfileDto tpDto = ThermocyclerProfileTestFixture.newThermocyclerProfile();
    tpDto.setSteps(List.of(RandomStringUtils.randomAlphabetic(1000)));
    assertThrows(ConstraintViolationException.class, () -> thermocyclerProfileRepository.create(tpDto));
  }

  @Test
  public void findThermocyclerProfile_whenNoFieldsAreSelected_productReturnedWithAllFields() {

    ThermocyclerProfile testThermocyclerProfile = createTestProfile();

    ThermocyclerProfileDto thermoDto = thermocyclerProfileRepository.findOne(testThermocyclerProfile.getUuid(), new QuerySpec(ThermocyclerProfileDto.class));
    assertNotNull(thermoDto);
    assertEquals(testThermocyclerProfile.getUuid(), thermoDto.getUuid());
    assertEquals(TEST_PROFILE_NAME, thermoDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, thermoDto.getCycles());

    assertThat(testThermocyclerProfile.getSteps(), contains(thermoDto.getSteps().toArray()));
  }
  
  @Test
  public void createAndPersistThermocyclerProfile_onSuccess_allFieldsHavePersistedValues() {

    //set a base DTO
    ThermocyclerProfileDto baseDto = ThermocyclerProfileTestFixture.newThermocyclerProfile();
    
    //create the DTO in the repository
    ThermocyclerProfileDto createdDto = thermocyclerProfileRepository.create(baseDto);
    
    //Assert DTO has the set values
    assertNotNull(createdDto.getUuid());
    assertEquals(baseDto.getName(), createdDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, createdDto.getCycles());
    assertThat(createdDto.getSteps(), contains(ThermocyclerProfileTestFixture.TEST_STEPS.toArray()));
    
    //Assert Entity has the set values
    ThermocyclerProfile profileEntity = thermocyclerProfileService.findOne(createdDto.getUuid(), ThermocyclerProfile.class);
    assertNotNull(profileEntity.getId());
    assertEquals(baseDto.getName(), profileEntity.getName());
    assertEquals(TEST_PROFILE_CYCLE, profileEntity.getCycles());
    assertThat(profileEntity.getSteps(), contains(ThermocyclerProfileTestFixture.TEST_STEPS.toArray()));
  }
  
  @Test
  public void updateThermocyclerProfile_dtoWithOnlyUpdatedFields_entityReturnedWithUpdatedFields() {
    ThermocyclerProfile testThermocyclerProfile = createTestProfile();
    QuerySpec querySpec = new QuerySpec(ThermocyclerProfileDto.class);
    
    ThermocyclerProfileDto thermoDto = thermocyclerProfileRepository.findOne(testThermocyclerProfile.getUuid(), querySpec);
    
    thermoDto.setCycles("new cycles");
    
    thermocyclerProfileRepository.save(thermoDto);
    
    assertEquals("new cycles", testThermocyclerProfile.getCycles());
  }

  @Test
  public void deleteThermocyclerProfile_callRepositoryDeleteOnID_profileNotFound() {
    ThermocyclerProfile testThermocyclerProfile = createTestProfile();
    thermocyclerProfileRepository.delete(testThermocyclerProfile.getUuid());
    assertNull(entityManager.find(ThermocyclerProfile.class, testThermocyclerProfile.getId()));
  }

  @Test
  public void deleteThermocyclerProfile_nonexistentID_throwsResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> thermocyclerProfileRepository.delete(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );
  }

}
