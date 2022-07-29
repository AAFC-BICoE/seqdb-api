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

import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
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

  private ThermocyclerProfile createTestProfile() {
    ThermocyclerProfile testThermocyclerProfile = ThermocyclerProfileFactory.newThermocyclerProfile()
        .name(TEST_PROFILE_NAME)
        .cycles(TEST_PROFILE_CYCLE)
        .steps(List.of("step1", "step2"))
        .build();
    persist(testThermocyclerProfile);
    return testThermocyclerProfile;
  }


  @Test
  public void findThermocyclerProfile_whenNoFieldsAreSelected_productReturnedWithAllFields() {

    ThermocyclerProfile testThermocyclerProfile = createTestProfile();

    ThermocyclerProfileDto thermoDto = thermocyclerProfileRepository.findOne(testThermocyclerProfile.getUuid(), new QuerySpec(ThermocyclerProfileDto.class));
    assertNotNull(thermoDto);
    assertEquals(testThermocyclerProfile.getUuid(), thermoDto.getUuid());
    assertEquals(TEST_PROFILE_NAME, thermoDto.getName());
    assertEquals(TEST_PROFILE_CYCLE, thermoDto.getCycles());

    System.out.println(testThermocyclerProfile.getSteps());
    System.out.println(thermoDto.getSteps());

    assertThat(testThermocyclerProfile.getSteps(), contains(thermoDto.getSteps().toArray()));
  }
  
  @Test
  public void createAndPersistThermocyclerProfile_onSuccess_allFieldsHavePersistedValues() {
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
    assertNull(createdDto.getSteps());
    
    //Assert Entity has the set values
    ThermocyclerProfile profileEntity = baseDao.findOneByNaturalId(createdDto.getUuid(), ThermocyclerProfile.class);
    assertNotNull(profileEntity.getId());
    assertEquals(newThermocyclerProfileName, profileEntity.getName());
    assertEquals(TEST_PROFILE_CYCLE, profileEntity.getCycles());
    assertNull(profileEntity.getSteps());
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
