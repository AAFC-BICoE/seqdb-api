package ca.gc.aafc.seqdb.api.mapper;

import java.util.Set;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisRunFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisRunItemFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunItemTestFixture;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.MolecularAnalysisRunTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MolecularAnalysisRunMapperTest {

  private final MolecularAnalysisRunMapper MAPPER = MolecularAnalysisRunMapper.INSTANCE;
  private final MolecularAnalysisRunItemMapper ITEM_MAPPER = MolecularAnalysisRunItemMapper.INSTANCE;

  @Test
  public void testToEntity() {
    MolecularAnalysisRunDto dto = MolecularAnalysisRunTestFixture.newMolecularAnalysisRun();
    MolecularAnalysisRun entity = MAPPER.toEntity(dto, Set.of("name"), null);

    assertEquals(dto.getName(), entity.getName());

    // --- Items ---
    MolecularAnalysisRunItemDto itemDto = MolecularAnalysisRunItemTestFixture.newMolecularAnalysisRunItem();
    MolecularAnalysisRunItem itemEntity = ITEM_MAPPER.toEntity(itemDto, Set.of("name"), null);

    assertEquals(itemDto.getName(), itemEntity.getName());
  }

  @Test
  public void testToDto() {
    MolecularAnalysisRun entity = MolecularAnalysisRunFactory.newMolecularAnalysisRun().build();
    MolecularAnalysisRunDto dto = MAPPER.toDto(entity, Set.of("name"), null);

    assertEquals( entity.getName(), dto.getName());

    // --- Items ---
    MolecularAnalysisRunItem itemEntity = MolecularAnalysisRunItemFactory.newMolecularAnalysisRunItem(entity).build();
    MolecularAnalysisRunItemDto itemDto = ITEM_MAPPER.toDto(itemEntity, Set.of("name", "run"), null);

    assertEquals( itemEntity.getName(), itemDto.getName());
    assertEquals( itemEntity.getRun().getName(), itemDto.getRun().getName());
  }

  @Test
  public void testPatchEntity() {
    MolecularAnalysisRun entity = MolecularAnalysisRunFactory.newMolecularAnalysisRun().build();
    MolecularAnalysisRunDto dto = MolecularAnalysisRunTestFixture.newMolecularAnalysisRun();

    MAPPER.patchEntity(entity, dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());

    // --- Items ---
    MolecularAnalysisRunItem itemEntity = MolecularAnalysisRunItemFactory.newMolecularAnalysisRunItem(null).build();
    MolecularAnalysisRunItemDto itemDto =MolecularAnalysisRunItemTestFixture.newMolecularAnalysisRunItem();
    itemDto.setRun(dto);
    ITEM_MAPPER.patchEntity(itemEntity, itemDto, Set.of("name", "run"), null);

    // Relationships are not set by the mapper
    assertNull(itemEntity.getRun());
  }
}
