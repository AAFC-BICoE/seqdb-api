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
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;

@Mapper(imports = MapperStaticConverter.class)
public interface MolecularAnalysisRunMapper extends DinaMapperV2<MolecularAnalysisRunDto, MolecularAnalysisRun> {

  MolecularAnalysisRunMapper INSTANCE = Mappers.getMapper(MolecularAnalysisRunMapper.class);

  @Mapping(target = "attachments", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getAttachments(), \"metadata\"))")
  MolecularAnalysisRunDto toDto(MolecularAnalysisRun entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  MolecularAnalysisRun toEntity(MolecularAnalysisRunDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget MolecularAnalysisRun entity, MolecularAnalysisRunDto dto,
                   @Context Set<String> provided, @Context String scope);
}
