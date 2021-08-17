package ca.gc.aafc.seqdb.api.testsupport.fixtures;

import ca.gc.aafc.seqdb.api.dto.MolecularSampleDto;
import ca.gc.aafc.seqdb.api.entities.MolecularSample;
import ca.gc.aafc.seqdb.api.testsupport.factories.TestableEntityFactory;

public class MolecularSampleTestFixture {

  private static final String GROUP = "aafc";
  private static final MolecularSample.SampleType SAMPLE_TYPE = MolecularSample.SampleType.DNA;
  
  public static MolecularSampleDto newMolecularSample() {
    MolecularSampleDto molecularSampleDto = new MolecularSampleDto();
    molecularSampleDto.setGroup(GROUP);
    molecularSampleDto.setCreatedBy("createdBy");
    molecularSampleDto.setName(TestableEntityFactory.generateRandomName(10));
    molecularSampleDto.setVersion("v1.0");
    molecularSampleDto.setSampleType(SAMPLE_TYPE);
    return molecularSampleDto;
  }
  
}
 