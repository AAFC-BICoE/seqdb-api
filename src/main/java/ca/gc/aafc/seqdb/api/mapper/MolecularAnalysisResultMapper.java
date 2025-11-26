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
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisResultDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;

@Mapper(imports = MapperStaticConverter.class)
public interface MolecularAnalysisResultMapper extends DinaMapperV2<MolecularAnalysisResultDto, MolecularAnalysisResult> {

  MolecularAnalysisResultMapper INSTANCE = Mappers.getMapper(MolecularAnalysisResultMapper.class);

  @Mapping(target = "attachments", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getAttachments(), \"metadata\"))")
  MolecularAnalysisResultDto toDto(MolecularAnalysisResult entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  MolecularAnalysisResult toEntity(MolecularAnalysisResultDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget MolecularAnalysisResult entity, MolecularAnalysisResultDto dto,
                   @Context Set<String> provided, @Context String scope);
}
