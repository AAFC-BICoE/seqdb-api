package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;

public class SeqReactionTestFixture {

  private static final String GROUP = "aafc";
  private static final String CREATED_BY = "createdBy";

  public static SeqReactionDto newSeqReaction() {
    SeqReactionDto seqReactionDto = new SeqReactionDto();
    seqReactionDto.setGroup(GROUP);
    seqReactionDto.setCreatedBy(CREATED_BY);
    return seqReactionDto;
  }
  
}

