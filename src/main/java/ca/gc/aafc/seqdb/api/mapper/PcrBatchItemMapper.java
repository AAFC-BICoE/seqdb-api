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
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;

@Mapper
public interface PcrBatchItemMapper extends DinaMapperV2<PcrBatchItemDto, PcrBatchItem> {

  PcrBatchItemMapper INSTANCE = Mappers.getMapper(PcrBatchItemMapper.class);

  PcrBatchItemDto toDto(PcrBatchItem entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  PcrBatchItem toEntity(PcrBatchItemDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget PcrBatchItem entity, PcrBatchItemDto dto,
                   @Context Set<String> provided, @Context String scope);
}

