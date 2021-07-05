package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;

public class PcrReactionTestFixture {

  private static final String GROUP = "aafc";

  public static PcrReactionDto newPcrReaction() {
    PcrReactionDto pcrReactionDto = new PcrReactionDto();
    pcrReactionDto.setGroup(GROUP);
    pcrReactionDto.setCreatedBy("createdBy");
    return pcrReactionDto;
  }
  
}
