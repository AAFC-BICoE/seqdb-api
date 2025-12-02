package ca.gc.aafc.seqdb.api.mapper;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.testsupport.factories.PcrPrimerFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.PcrPrimerTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

public class PcrPrimerMapperTest {

  private final PcrPrimerMapper MAPPER = PcrPrimerMapper.INSTANCE;

  @Test
  public void testToEntity() {
    PcrPrimerDto dto = PcrPrimerTestFixture.newPcrPrimer();
    PcrPrimer entity = MAPPER.toEntity(dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());
  }

  @Test
  public void testToDto() {
    PcrPrimer entity = PcrPrimerFactory.newPcrPrimer().build();
    PcrPrimerDto dto = MAPPER.toDto(entity, Set.of("name"), null);

    assertEquals( entity.getName(), dto.getName());
  }

  @Test
  public void testPatchEntity() {
    PcrPrimer entity = PcrPrimerFactory.newPcrPrimer().build();
    PcrPrimerDto dto = PcrPrimerTestFixture.newPcrPrimer();

    MAPPER.patchEntity(entity, dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());
  }
}
