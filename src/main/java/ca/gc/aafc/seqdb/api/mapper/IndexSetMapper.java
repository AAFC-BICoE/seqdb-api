package ca.gc.aafc.seqdb.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;

import java.util.Set;

@Mapper
public interface IndexSetMapper extends DinaMapperV2<IndexSetDto, IndexSet> {

  IndexSetMapper INSTANCE = Mappers.getMapper(IndexSetMapper.class);

  IndexSetDto toDto(IndexSet entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ngsIndexes", ignore = true)
  IndexSet toEntity(IndexSetDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "ngsIndexes", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget IndexSet entity, IndexSetDto dto,
                   @Context Set<String> provided, @Context String scope);
}
