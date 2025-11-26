package ca.gc.aafc.seqdb.api.mapper;

import java.util.Set;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.dina.mapper.MapperStaticConverter;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;

@Mapper(imports = MapperStaticConverter.class)
public interface GenericMolecularAnalysisMapper extends DinaMapperV2<GenericMolecularAnalysisDto, GenericMolecularAnalysis> {

  GenericMolecularAnalysisMapper INSTANCE = Mappers.getMapper(GenericMolecularAnalysisMapper.class);

  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  GenericMolecularAnalysisDto toDto(GenericMolecularAnalysis entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  GenericMolecularAnalysis toEntity(GenericMolecularAnalysisDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget GenericMolecularAnalysis entity, GenericMolecularAnalysisDto dto,
                   @Context Set<String> provided, @Context String scope);
}
