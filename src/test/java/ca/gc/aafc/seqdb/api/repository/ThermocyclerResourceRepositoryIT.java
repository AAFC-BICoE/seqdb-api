package ca.gc.aafc.seqdb.api.repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import ca.gc.aafc.dina.exception.ConflictException;
import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocuments;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.service.ThermocyclerProfileService;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.ThermocyclerProfileTestFixture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;

public class ThermocyclerResourceRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private ThermocyclerProfileService thermocyclerProfileService;

  @Inject
  private ThermocyclerProfileRepository thermocyclerProfileRepository;

  @Test
  public void createThermocyclerProfile_whenStepsSizeExceeded_ExceptionThrown() {
    ThermocyclerProfileDto tpDto = ThermocyclerProfileTestFixture.newThermocyclerProfile();
    tpDto.setSteps(List.of(RandomStringUtils.randomAlphabetic(1000)));
    assertThrows(ConstraintViolationException.class, () -> createWithRepository(tpDto, thermocyclerProfileRepository::onCreate));
  }

  @Test
  public void findThermocyclerProfile_whenNoFieldsAreSelected_returnedWithAllFields()
      throws ResourceGoneException, ResourceNotFoundException {

    ThermocyclerProfileDto tpDto = ThermocyclerProfileTestFixture.newThermocyclerProfile();
    UUID tpUuid = createWithRepository(tpDto, thermocyclerProfileRepository::onCreate);

    ThermocyclerProfileDto thermoDto = thermocyclerProfileRepository.getOne(tpUuid, "").getDto();
    assertNotNull(thermoDto);
    assertEquals(tpUuid, thermoDto.getUuid());
    assertEquals(tpDto.getName(), thermoDto.getName());
    assertEquals(tpDto.getCycles(), thermoDto.getCycles());

    assertThat(ThermocyclerProfileTestFixture.TEST_STEPS, contains(thermoDto.getSteps().toArray()));
  }

  @Test
  public void updateThermocyclerProfile_dtoWithOnlyUpdatedFields_entityReturnedWithUpdatedFields()
      throws ResourceGoneException, ResourceNotFoundException, ConflictException {
    ThermocyclerProfileDto tpDto = ThermocyclerProfileTestFixture.newThermocyclerProfile();
    UUID tpUuid = createWithRepository(tpDto, thermocyclerProfileRepository::onCreate);

    JsonApiDocument thermocyclerProfileToUpdate = JsonApiDocuments.createJsonApiDocument(tpUuid,
      ThermocyclerProfileDto.TYPENAME, Map.of("cycles", "new cycles"));
    thermocyclerProfileRepository.onUpdate(thermocyclerProfileToUpdate, tpUuid);

    ThermocyclerProfileDto reloadedDto = thermocyclerProfileRepository.getOne(tpUuid, "").getDto();
    assertEquals("new cycles", reloadedDto.getCycles());
  }

  @Test
  public void deleteThermocyclerProfile_callRepositoryDeleteOnID_profileNotFound()
      throws ResourceGoneException, ResourceNotFoundException {
    ThermocyclerProfileDto tpDto = ThermocyclerProfileTestFixture.newThermocyclerProfile();
    UUID tpUuid = createWithRepository(tpDto, thermocyclerProfileRepository::onCreate);
    thermocyclerProfileRepository.onDelete(tpUuid);

    assertThrows(ResourceNotFoundException.class, () -> thermocyclerProfileRepository.getOne(
      tpUuid, ""
    ));
  }

  @Test
  public void deleteThermocyclerProfile_nonexistentID_throwsResourceNotFoundException() {
    assertThrows(
      ResourceNotFoundException.class,
      () -> thermocyclerProfileRepository.onDelete(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    );
  }
}
