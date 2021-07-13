package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;

public class PcrReactionTestFixture {

  public static final String GROUP = "aafc";
  public static final String RESULT = "F10, weak band";
  public static final String TARGET = "16S";

  public static PcrReactionDto newPcrReaction() {
    PcrReactionDto pcrReactionDto = new PcrReactionDto();
    pcrReactionDto.setGroup(GROUP);
    pcrReactionDto.setCreatedBy("createdBy");
    pcrReactionDto.setResult(RESULT);
    pcrReactionDto.setTarget(TARGET);
    return pcrReactionDto;
  }
  
}
