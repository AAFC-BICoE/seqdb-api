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
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchDto;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatch;

@Mapper
public interface PcrBatchMapper extends DinaMapperV2<PcrBatchDto, PcrBatch> {

  PcrBatchMapper INSTANCE = Mappers.getMapper(PcrBatchMapper.class);

  PcrBatchDto toDto(PcrBatch entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  PcrBatch toEntity(PcrBatchDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget PcrBatch entity, PcrBatchDto dto,
                   @Context Set<String> provided, @Context String scope);
}
