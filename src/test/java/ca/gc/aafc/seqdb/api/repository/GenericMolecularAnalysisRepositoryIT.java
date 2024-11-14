package ca.gc.aafc.seqdb.api.repository;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.SequenceVocabularyConfiguration;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisDto;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisItemDto;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.GenericMolecularAnalysisItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.GenericMolecularAnalysisTestFixture;

import javax.inject.Inject;

public class GenericMolecularAnalysisRepositoryIT extends BaseRepositoryTestV2 {

  @Inject
  private GenericMolecularAnalysisRepository genericMolecularAnalysisRepository;

  @Inject
  private GenericMolecularAnalysisItemRepository genericMolecularAnalysisItemRepository;

  @Test
  public void onValidDto_dtoSavedWithoutExceptions() {

    GenericMolecularAnalysisDto genericMolecularAnalysisDto = genericMolecularAnalysisRepository
      .create(GenericMolecularAnalysisTestFixture.newGenericMolecularAnalysis());

    GenericMolecularAnalysisItemDto itemDto = GenericMolecularAnalysisItemTestFixture
      .newGenericMolecularAnalysisItem();

    itemDto.setGenericMolecularAnalysis(genericMolecularAnalysisDto);

    genericMolecularAnalysisItemRepository.create(itemDto);
  }
}
