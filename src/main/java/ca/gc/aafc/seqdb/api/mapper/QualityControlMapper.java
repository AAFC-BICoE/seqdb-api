package ca.gc.aafc.seqdb.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.dto.QualityControlDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;
import ca.gc.aafc.seqdb.api.entities.QualityControl;

import java.util.Set;

@Mapper
public interface QualityControlMapper extends DinaMapperV2<QualityControlDto, QualityControl> {

  QualityControlMapper INSTANCE = Mappers.getMapper(QualityControlMapper.class);

  QualityControlDto toDto(QualityControl entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  QualityControl toEntity(QualityControlDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "molecularAnalysisRunItem", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget QualityControl entity, QualityControlDto dto,
                   @Context Set<String> provided, @Context String scope);

  default MolecularAnalysisRunItemDto toDto(MolecularAnalysisRunItem entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toMolecularAnalysisRunDto(entity, provided, "molecularAnalysisRunItem");
  }

  @Mapping(target = "run", ignore = true)
  @Mapping(target = "result", ignore = true)
  MolecularAnalysisRunItemDto toMolecularAnalysisRunDto(MolecularAnalysisRunItem entity, Set<String> provided, String scope);

}
