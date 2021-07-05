package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MolecularSampleDto;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class MolecularSampleTestFixture {

  private static final String GROUP = "aafc";
  
  public static MolecularSampleDto newMolecularSample() {
    MolecularSampleDto molecularSampleDto = new MolecularSampleDto();
    molecularSampleDto.setGroup(GROUP);
    molecularSampleDto.setCreatedBy("createdBy");
    molecularSampleDto.setName(TestableEntityFactory.generateRandomName(10));
    molecularSampleDto.setVersion("v1.0");
    return molecularSampleDto;
  }
  
}
