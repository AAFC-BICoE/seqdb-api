package ca.gc.aafc.seqdb.api.mapper;

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
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisItemDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;

import java.util.Set;

@Mapper(imports = MapperStaticConverter.class)
public interface GenericMolecularAnalysisItemMapper extends DinaMapperV2<GenericMolecularAnalysisItemDto, GenericMolecularAnalysisItem> {

  GenericMolecularAnalysisItemMapper INSTANCE = Mappers.getMapper(GenericMolecularAnalysisItemMapper.class);

  @Mapping(target = "materialSample", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getMaterialSample(), \"material-sample\"))")
  @Mapping(target = "storageUnitUsage", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnitUsage(), \"storage-unit-usage\"))")
  GenericMolecularAnalysisItemDto toDto(GenericMolecularAnalysisItem entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "materialSample", ignore = true)
  @Mapping(target = "storageUnitUsage", ignore = true)
  @Mapping(target = "genericMolecularAnalysis", ignore = true)
  @Mapping(target = "molecularAnalysisRunItem", ignore = true)
  GenericMolecularAnalysisItem toEntity(GenericMolecularAnalysisItemDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "materialSample", ignore = true)
  @Mapping(target = "storageUnitUsage", ignore = true)
  @Mapping(target = "genericMolecularAnalysis", ignore = true)
  @Mapping(target = "molecularAnalysisRunItem", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget GenericMolecularAnalysisItem entity, GenericMolecularAnalysisItemDto dto,
                   @Context Set<String> provided, @Context String scope);


  default GenericMolecularAnalysisDto toDto(GenericMolecularAnalysis entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toGenericMolecularAnalysisDto(entity, provided, "genericMolecularAnalysis");
  }
  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  GenericMolecularAnalysisDto toGenericMolecularAnalysisDto(GenericMolecularAnalysis entity, Set<String> provided, String scope);

  default MolecularAnalysisRunItemDto toDto(MolecularAnalysisRunItem entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toMolecularAnalysisRunItemDto(entity, provided, "molecularAnalysisRunItem");
  }

  @Mapping(target = "result", ignore = true)
  @Mapping(target = "run", ignore = true)
  MolecularAnalysisRunItemDto toMolecularAnalysisRunItemDto(MolecularAnalysisRunItem entity, Set<String> provided, String scope);

}
