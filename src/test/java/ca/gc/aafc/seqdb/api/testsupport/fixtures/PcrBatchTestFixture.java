package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import java.time.OffsetDateTime;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class PcrBatchTestFixture {

  public static final String GROUP = "aafc";
  public static final String CREATED_BY = "createdBy";
  public static final String THERMOCYCLER = "room 2035 #2";
  public static final String OBJECTIVE = "Amplification of Johnathan collection 2018 samples";
  public static final String POSITIVE_CONTROL = "LM1044";
  public static final String REACTION_VOLUME = "10µl";
  public static final OffsetDateTime REACTION_DATE = OffsetDateTime.now();

  public static PcrBatchDto newPcrBatch() {
    PcrBatchDto pcrBatchDto = new PcrBatchDto();
    pcrBatchDto.setGroup(GROUP);
    pcrBatchDto.setCreatedBy(CREATED_BY);
    pcrBatchDto.setName(TestableEntityFactory.generateRandomName(10));
    pcrBatchDto.setThermocycler(THERMOCYCLER);
    pcrBatchDto.setObjective(OBJECTIVE);
    pcrBatchDto.setPositiveControl(POSITIVE_CONTROL);
    pcrBatchDto.setReactionVolume(REACTION_VOLUME);
    pcrBatchDto.setReactionDate(REACTION_DATE);
    return pcrBatchDto;
  }
  
}
