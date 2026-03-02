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
import ca.gc.aafc.dina.mapper.MapperStaticConverter;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;

@Mapper(imports = MapperStaticConverter.class)
public interface LibraryPoolContentMapper extends DinaMapperV2<LibraryPoolContentDto, LibraryPoolContent> {

  LibraryPoolContentMapper INSTANCE = Mappers.getMapper(LibraryPoolContentMapper.class);

  LibraryPoolContentDto toDto(LibraryPoolContent entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPool", ignore = true)
  @Mapping(target = "pooledLibraryPrepBatch", ignore = true)
  @Mapping(target = "pooledLibraryPool", ignore = true)
  LibraryPoolContent toEntity(LibraryPoolContentDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPool", ignore = true)
  @Mapping(target = "pooledLibraryPrepBatch", ignore = true)
  @Mapping(target = "pooledLibraryPool", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget LibraryPoolContent entity, LibraryPoolContentDto dto,
                   @Context Set<String> provided, @Context String scope);


  /**
   *   @NotNull
   *   @ManyToOne(fetch = FetchType.LAZY)
   *   @JoinColumn(name = "librarypoolid")
   *   private LibraryPool libraryPool;
   *
   *   @ManyToOne(fetch = FetchType.LAZY)
   *   @JoinColumn(name = "pooledlibraryprepbatchid")
   *   private LibraryPrepBatch pooledLibraryPrepBatch;
   *
   *   @ManyToOne(fetch = FetchType.LAZY)
   *   @JoinColumn(name = "pooledlibrarypoolid")
   *   private LibraryPool pooledLibraryPool;
   */

  // --- libraryPool ---
  default LibraryPoolDto toDto(LibraryPool entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toLibraryPoolDto(entity, provided, "libraryPool");
  }

  @Mapping(target = "contents", ignore = true)
  LibraryPoolDto toLibraryPoolDto(LibraryPool entity, Set<String> provided, String scope);

  // --- pooledLibraryPrepBatch ---
  default LibraryPrepBatchDto toDto(LibraryPrepBatch entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toLibraryPrepBatchDto(entity, provided, "pooledLibraryPrepBatch");
  }

  @Mapping(target = "libraryPreps", ignore = true)
  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnit(), \"storage-unit\"))")
  LibraryPrepBatchDto toLibraryPrepBatchDto(LibraryPrepBatch entity, Set<String> provided, String scope);

}
