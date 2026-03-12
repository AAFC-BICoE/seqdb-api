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
import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;

@Mapper
public interface PreLibraryPrepMapper extends DinaMapperV2<PreLibraryPrepDto, PreLibraryPrep> {

  PreLibraryPrepMapper INSTANCE = Mappers.getMapper(PreLibraryPrepMapper.class);

  PreLibraryPrepDto toDto(PreLibraryPrep entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  PreLibraryPrep toEntity(PreLibraryPrepDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget PreLibraryPrep entity, PreLibraryPrepDto dto,
                   @Context Set<String> provided, @Context String scope);
}

