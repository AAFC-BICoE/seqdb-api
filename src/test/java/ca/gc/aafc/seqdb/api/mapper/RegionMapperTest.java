package ca.gc.aafc.seqdb.api.mapper;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.entities.Region;
import ca.gc.aafc.seqdb.api.testsupport.factories.RegionFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.RegionTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

public class RegionMapperTest {

  private final RegionMapper MAPPER = RegionMapper.INSTANCE;

  @Test
  public void testToEntity() {
    RegionDto dto = RegionTestFixture.newRegion();
    Region entity = MAPPER.toEntity(dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());
  }

  @Test
  public void testToDto() {
    Region entity = RegionFactory.newRegion().build();
    RegionDto dto = MAPPER.toDto(entity, Set.of("name"), null);

    assertEquals( entity.getName(), dto.getName());
  }

  @Test
  public void testPatchEntity() {
    Region entity = RegionFactory.newRegion().build();
    RegionDto dto = RegionTestFixture.newRegion();

    MAPPER.patchEntity(entity, dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());
  }
}
