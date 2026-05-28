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
import ca.gc.aafc.seqdb.api.dto.NgsIndexDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.NgsIndex;

@Mapper
public interface NgsIndexMapper extends DinaMapperV2<NgsIndexDto, NgsIndex> {

  NgsIndexMapper INSTANCE = Mappers.getMapper(NgsIndexMapper.class);

  NgsIndexDto toDto(NgsIndex entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "indexSet", ignore = true)
  NgsIndex toEntity(NgsIndexDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "indexSet", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget NgsIndex entity, NgsIndexDto dto,
                   @Context Set<String> provided, @Context String scope);
}
