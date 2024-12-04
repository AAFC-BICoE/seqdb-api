package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.dto.QualityControlDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.QualityControlTestFixture;

import javax.inject.Inject;

public class QualityControlRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private QualityControlRepository qualityControlRepository;

  @Inject
  private MolecularAnalysisRunRepository molecularAnalysisRunRepository;

  @Inject
  private MolecularAnalysisRunItemRepository molecularAnalysisRunItemRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    MolecularAnalysisRunDto runDto = molecularAnalysisRunRepository
      .create(MolecularAnalysisRunTestFixture.newMolecularAnalysisRun());

    MolecularAnalysisRunItemDto runItemDto = MolecularAnalysisRunItemTestFixture
      .newMolecularAnalysisRunItem();
    runItemDto.setRun(runDto);
    molecularAnalysisRunItemRepository.create(runItemDto);

    QualityControlDto qualityControlDto = QualityControlTestFixture.newQualityControl();
    qualityControlDto.setMolecularAnalysisRunItem(runItemDto);

    qualityControlRepository.create(qualityControlDto);
  }

}
