package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class ThermocyclerProfileTestFixture {

  public static final String TEST_PROFILE_CYCLE = "Cycle";
  public static List<String> TEST_STEPS = List.of("step1", "step2");

  public static ThermocyclerProfileDto newThermocyclerProfile() {
    ThermocyclerProfileDto thermocyclerProfileDto = new ThermocyclerProfileDto();
    thermocyclerProfileDto.setName(RandomStringUtils.randomAlphabetic(25));
    thermocyclerProfileDto.setCycles(TEST_PROFILE_CYCLE);
    thermocyclerProfileDto.setSteps(TEST_STEPS);
    return thermocyclerProfileDto;
  }
}
