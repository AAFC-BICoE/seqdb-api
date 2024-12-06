package ca.gc.aafc.seqdb.api.testsupport.factories;

import java.util.UUID;

import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

public class MolecularAnalysisRunItemFactory {

  public static MolecularAnalysisRunItem.MolecularAnalysisRunItemBuilder newMolecularAnalysisRunItem(MolecularAnalysisRun run) {

    return MolecularAnalysisRunItem.builder()
      .uuid(UUID.randomUUID())
      .createdBy("test user")
      .usageType(SeqReactionDto.TYPENAME)
      .name("test-name")
      .run(run);
  }
}
