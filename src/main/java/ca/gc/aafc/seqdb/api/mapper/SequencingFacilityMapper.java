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
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.entities.SequencingFacility;

@Mapper
public interface SequencingFacilityMapper extends DinaMapperV2<SequencingFacilityDto, SequencingFacility> {

  SequencingFacilityMapper INSTANCE = Mappers.getMapper(SequencingFacilityMapper.class);

  SequencingFacilityDto toDto(SequencingFacility entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  SequencingFacility toEntity(SequencingFacilityDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SequencingFacility entity, SequencingFacilityDto dto,
                   @Context Set<String> provided, @Context String scope);
}

