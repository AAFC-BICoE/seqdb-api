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
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;

@Mapper
public interface ThermocyclerProfileMapper extends DinaMapperV2<ThermocyclerProfileDto, ThermocyclerProfile> {

  ThermocyclerProfileMapper INSTANCE = Mappers.getMapper(ThermocyclerProfileMapper.class);

  ThermocyclerProfileDto toDto(ThermocyclerProfile entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  ThermocyclerProfile toEntity(ThermocyclerProfileDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget ThermocyclerProfile entity, ThermocyclerProfileDto dto,
                   @Context Set<String> provided, @Context String scope);
}
