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
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;

@Mapper(imports = MapperStaticConverter.class)
public interface PcrBatchItemMapper extends DinaMapperV2<PcrBatchItemDto, PcrBatchItem> {

  PcrBatchItemMapper INSTANCE = Mappers.getMapper(PcrBatchItemMapper.class);

  @Mapping(target = "materialSample", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getMaterialSample(), \"material-sample\"))")
  @Mapping(target = "storageUnitUsage", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnitUsage(), \"storage-unit-usage\"))")
  PcrBatchItemDto toDto(PcrBatchItem entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pcrBatch", ignore = true)
  PcrBatchItem toEntity(PcrBatchItemDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "pcrBatch", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget PcrBatchItem entity, PcrBatchItemDto dto,
                   @Context Set<String> provided, @Context String scope);

  default PcrBatchDto toDto(PcrBatch entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toPcrBatchDto(entity, provided, "pcrBatch");
  }

  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  @Mapping(target = "attachment", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getAttachment(), \"metadata\"))")
  @Mapping(target = "experimenters", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getExperimenters(), \"person\"))")
  @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnit(), \"storage-unit\"))")
  PcrBatchDto toPcrBatchDto(PcrBatch entity, Set<String> provided, String scope);

}
