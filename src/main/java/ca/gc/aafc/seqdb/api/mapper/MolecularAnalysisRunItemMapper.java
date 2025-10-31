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
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

@Mapper(imports = MapperStaticConverter.class)
public interface MolecularAnalysisRunItemMapper extends DinaMapperV2<MolecularAnalysisRunItemDto, MolecularAnalysisRunItem> {

  MolecularAnalysisRunItemMapper INSTANCE = Mappers.getMapper(MolecularAnalysisRunItemMapper.class);

  MolecularAnalysisRunItemDto toDto(MolecularAnalysisRunItem entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  MolecularAnalysisRunItem toEntity(MolecularAnalysisRunItemDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "run", ignore = true)
  @Mapping(target = "result", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget MolecularAnalysisRunItem entity, MolecularAnalysisRunItemDto dto,
                   @Context Set<String> provided, @Context String scope);

  default MolecularAnalysisRunDto toDto(MolecularAnalysisRun entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toMolecularAnalysisRunDto(entity, provided, "run");
  }
  @Mapping(target = "attachments", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getAttachments(), \"attachments\"))")
  MolecularAnalysisRunDto toMolecularAnalysisRunDto(MolecularAnalysisRun entity, Set<String> provided, String scope);

  default MolecularAnalysisResultDto toDto(MolecularAnalysisResult entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toMolecularAnalysisResultDto(entity, provided, "result");
  }
  @Mapping(target = "attachments", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getAttachments(), \"attachments\"))")
  MolecularAnalysisResultDto toMolecularAnalysisResultDto(MolecularAnalysisResult entity, Set<String> provided, String scope);
}
