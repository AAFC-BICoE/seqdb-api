package ca.gc.aafc.seqdb.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.dina.mapper.MapperStaticConverter;
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchDto;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatch;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;

import java.util.Set;

@Mapper(imports = MapperStaticConverter.class)
public interface MetagenomicsBatchMapper extends DinaMapperV2<MetagenomicsBatchDto, MetagenomicsBatch> {

  MetagenomicsBatchMapper INSTANCE = Mappers.getMapper(MetagenomicsBatchMapper.class);

  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  MetagenomicsBatchDto toDto(MetagenomicsBatch entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "indexSet", ignore = true)
  MetagenomicsBatch toEntity(MetagenomicsBatchDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "indexSet", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget MetagenomicsBatch entity, MetagenomicsBatchDto dto,
                   @Context Set<String> provided, @Context String scope);

  default IndexSetDto toDto(IndexSet entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toIndexSetDto(entity, provided, "indexSet");
  }

  @Mapping(target = "ngsIndexes", ignore = true)
  IndexSetDto toIndexSetDto(IndexSet entity, Set<String> provided, String scope);
}

