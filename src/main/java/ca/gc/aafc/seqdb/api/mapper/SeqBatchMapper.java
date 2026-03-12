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
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;

@Mapper
public interface SeqBatchMapper extends DinaMapperV2<SeqBatchDto, SeqBatch> {

  SeqBatchMapper INSTANCE = Mappers.getMapper(SeqBatchMapper.class);

  SeqBatchDto toDto(SeqBatch entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  SeqBatch toEntity(SeqBatchDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SeqBatch entity, SeqBatchDto dto,
                   @Context Set<String> provided, @Context String scope);
}
