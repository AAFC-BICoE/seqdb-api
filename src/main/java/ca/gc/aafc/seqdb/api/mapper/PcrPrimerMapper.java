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
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;

@Mapper
public interface PcrPrimerMapper extends DinaMapperV2<PcrPrimerDto, PcrPrimer> {

  PcrPrimerMapper INSTANCE = Mappers.getMapper(PcrPrimerMapper.class);

  PcrPrimerDto toDto(PcrPrimer entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  PcrPrimer toEntity(PcrPrimerDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "region", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget PcrPrimer entity, PcrPrimerDto dto,
                   @Context Set<String> provided, @Context String scope);
}
