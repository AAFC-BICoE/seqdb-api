package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisResultDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisResultFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunTestFixture;

import javax.inject.Inject;

/**
 * Tests for all MolecularAnalysisRun-based entities.
 */
public class MolecularAnalysisRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private MolecularAnalysisRunRepository molecularAnalysisRunRepository;

  @Inject
  private MolecularAnalysisRunItemRepository molecularAnalysisRunItemRepository;

  @Inject
  private MolecularAnalysisResultRepository molecularAnalysisResultRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    MolecularAnalysisRunDto runDto = molecularAnalysisRunRepository
      .create(MolecularAnalysisRunTestFixture.newMolecularAnalysisRun());

    MolecularAnalysisResultDto resultDto = molecularAnalysisResultRepository
      .create(MolecularAnalysisResultFixture.newMolecularAnalysisResult());

    MolecularAnalysisRunItemDto runItemDto = MolecularAnalysisRunItemTestFixture
      .newMolecularAnalysisRunItem();
    runItemDto.setRun(runDto);
    runItemDto.setResult(resultDto);

    molecularAnalysisRunItemRepository.create(runItemDto);
  }
}
