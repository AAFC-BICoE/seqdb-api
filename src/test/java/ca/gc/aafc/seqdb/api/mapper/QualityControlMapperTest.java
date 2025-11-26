package ca.gc.aafc.seqdb.api.mapper;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.QualityControlDto;
import ca.gc.aafc.seqdb.api.entities.QualityControl;
import ca.gc.aafc.seqdb.api.testsupport.factories.QualityControlFactory;
import ca.gc.aafc.seqdb.api.testsupport.fixtures.QualityControlTestFixture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

public class QualityControlMapperTest {

  private final QualityControlMapper MAPPER =  QualityControlMapper.INSTANCE;

  @Test
  public void testToEntity() {
    QualityControlDto dto = QualityControlTestFixture.newQualityControl();
    QualityControl entity = MAPPER.toEntity(dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());
  }

  @Test
  public void testToDto() {
    QualityControl entity = QualityControlFactory.newQualityControl().build();
    QualityControlDto dto = MAPPER.toDto(entity, Set.of("name"), null);

    assertEquals( entity.getName(), dto.getName());
  }

  @Test
  public void testPatchEntity() {
    QualityControl entity = QualityControlFactory.newQualityControl().build();
    QualityControlDto dto = QualityControlTestFixture.newQualityControl();

    MAPPER.patchEntity(entity, dto, Set.of("name"), null);
    assertEquals(dto.getName(), entity.getName());
  }
}
