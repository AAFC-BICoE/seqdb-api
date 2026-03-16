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
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;

@Mapper(imports = MapperStaticConverter.class)
public interface PcrBatchMapper extends DinaMapperV2<PcrBatchDto, PcrBatch> {

  PcrBatchMapper INSTANCE = Mappers.getMapper(PcrBatchMapper.class);

  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  @Mapping(target = "attachment", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getAttachment(), \"metadata\"))")
  @Mapping(target = "experimenters", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getExperimenters(), \"person\"))")
  @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnit(), \"storage-unit\"))")
  PcrBatchDto toDto(PcrBatch entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  @Mapping(target = "primerForward", ignore = true)
  @Mapping(target = "primerReverse", ignore = true)
  @Mapping(target = "thermocyclerProfile", ignore = true)
  PcrBatch toEntity(PcrBatchDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  @Mapping(target = "primerForward", ignore = true)
  @Mapping(target = "primerReverse", ignore = true)
  @Mapping(target = "thermocyclerProfile", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget PcrBatch entity, PcrBatchDto dto,
                   @Context Set<String> provided, @Context String scope);
}
