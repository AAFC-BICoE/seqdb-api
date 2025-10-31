package ca.gc.aafc.seqdb.api.mapper;

import java.util.Set;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.testsupport.factories.GenericMolecularAnalysisFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.GenericMolecularAnalysisTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericMolecularAnalysisMapperTest {

  private final GenericMolecularAnalysisMapper MAPPER = GenericMolecularAnalysisMapper.INSTANCE;

  @Test
  public void testToEntity() {
    GenericMolecularAnalysisDto dto = GenericMolecularAnalysisTestFixture.newGenericMolecularAnalysis();
    GenericMolecularAnalysis entity = MAPPER.toEntity(dto, Set.of("name"), null);

    assertEquals(dto.getName(), entity.getName());
  }

  @Test
  public void testToDto() {
    GenericMolecularAnalysis entity = GenericMolecularAnalysisFactory.newGenericMolecularAnalysis().build();
    GenericMolecularAnalysisDto dto = MAPPER.toDto(entity, Set.of("name"), null);

    assertEquals( entity.getName(), dto.getName());
  }

  @Test
  public void testPatchEntity() {
    GenericMolecularAnalysis entity = GenericMolecularAnalysisFactory.newGenericMolecularAnalysis().build();
    GenericMolecularAnalysisDto dto = GenericMolecularAnalysisTestFixture.newGenericMolecularAnalysis();

    MAPPER.patchEntity(entity, dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());
  }
}
