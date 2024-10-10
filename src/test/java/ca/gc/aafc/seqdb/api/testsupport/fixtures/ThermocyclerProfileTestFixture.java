package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class ThermocyclerProfileTestFixture {

  public static final String TEST_PROFILE_CYCLE = "cycle";
  public static List<String> TEST_STEPS = List.of("step1", "step2");

  public static ThermocyclerProfileDto newThermocyclerProfile() {

    ThermocyclerProfileDto thermocyclerProfileDto = new ThermocyclerProfileDto();

    thermocyclerProfileDto.setCreatedBy("createdBy");
    thermocyclerProfileDto.setCreatedOn(OffsetDateTime.of(LocalDate.now(), LocalTime.now(), ZoneOffset.MIN));
    thermocyclerProfileDto.setGroup("aafc");
    thermocyclerProfileDto.setName(RandomStringUtils.randomAlphabetic(25));
    thermocyclerProfileDto.setApplication("application");
    thermocyclerProfileDto.setCycles(TEST_PROFILE_CYCLE);
    thermocyclerProfileDto.setLastModified(Timestamp.valueOf(LocalDateTime.now()));
    thermocyclerProfileDto.setSteps(TEST_STEPS);
    return thermocyclerProfileDto;
  }
}
