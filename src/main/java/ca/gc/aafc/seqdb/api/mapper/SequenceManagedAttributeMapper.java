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
import ca.gc.aafc.seqdb.api.dto.SequenceManagedAttributeDto;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;

@Mapper
public interface SequenceManagedAttributeMapper extends DinaMapperV2<SequenceManagedAttributeDto, SequenceManagedAttribute> {

  SequenceManagedAttributeMapper INSTANCE = Mappers.getMapper(SequenceManagedAttributeMapper.class);

  SequenceManagedAttributeDto toDto(SequenceManagedAttribute entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  SequenceManagedAttribute toEntity(SequenceManagedAttributeDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SequenceManagedAttribute entity, SequenceManagedAttributeDto dto,
                   @Context Set<String> provided, @Context String scope);
}
