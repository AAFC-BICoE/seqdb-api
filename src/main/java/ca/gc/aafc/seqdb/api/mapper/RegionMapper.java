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
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.entities.Region;

@Mapper
public interface RegionMapper extends DinaMapperV2<RegionDto, Region> {

  RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

  RegionDto toDto(Region entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  Region toEntity(RegionDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget Region entity, RegionDto dto,
                   @Context Set<String> provided, @Context String scope);
}
