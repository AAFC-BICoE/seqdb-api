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
import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;

@Mapper
public interface LibraryPoolMapper extends DinaMapperV2<LibraryPoolDto, LibraryPool> {

  LibraryPoolMapper INSTANCE = Mappers.getMapper(LibraryPoolMapper.class);

  LibraryPoolDto toDto(LibraryPool entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "contents", ignore = true)
  LibraryPool toEntity(LibraryPoolDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "contents", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget LibraryPool entity, LibraryPoolDto dto,
                   @Context Set<String> provided, @Context String scope);


  default LibraryPoolContentDto toDto(LibraryPoolContent entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toLibraryPoolContentDto(entity, provided, "contents");
  }

  @Mapping(target = "libraryPool", ignore = true)
  @Mapping(target = "pooledLibraryPrepBatch", ignore = true)
  @Mapping(target = "pooledLibraryPool", ignore = true)
  LibraryPoolContentDto toLibraryPoolContentDto(LibraryPoolContent entity, Set<String> provided, String scope);
}
