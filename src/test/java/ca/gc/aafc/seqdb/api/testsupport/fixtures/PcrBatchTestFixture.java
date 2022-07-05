package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import java.time.LocalDate;
import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class PcrBatchTestFixture {

  public static final String GROUP = "aafc";
  public static final String CREATED_BY = "createdBy";
  public static final String THERMOCYCLER = "room 2035 #2";
  public static final String OBJECTIVE = "Amplification of Johnathan collection 2018 samples";
  public static final String POSITIVE_CONTROL = "LM1044";
  public static final String REACTION_VOLUME = "10µl";
  public static final LocalDate REACTION_DATE = LocalDate.now();
  public static final UUID STORAGE_UNIT = UUID.randomUUID();

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
    pcrBatchDto.setStorageUnit(ExternalRelationDto.builder().id(STORAGE_UNIT.toString()).type("storage-unit").build());
    return pcrBatchDto;
  }
  
}
