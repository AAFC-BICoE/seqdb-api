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
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;

@Mapper(imports = MapperStaticConverter.class)
public interface LibraryPrepMapper extends DinaMapperV2<LibraryPrepDto, LibraryPrep> {

  LibraryPrepMapper INSTANCE = Mappers.getMapper(LibraryPrepMapper.class);

  @Mapping(target = "storageUnitUsage", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnitUsage(), \"storage-unit-usage\"))")
  @Mapping(target = "materialSample", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getMaterialSample(), \"material-sample\"))")
  LibraryPrepDto toDto(LibraryPrep entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPrepBatch", ignore = true)
  @Mapping(target = "indexI5", ignore = true)
  @Mapping(target = "indexI7", ignore = true)
  LibraryPrep toEntity(LibraryPrepDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPrepBatch", ignore = true)
  @Mapping(target = "indexI5", ignore = true)
  @Mapping(target = "indexI7", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget LibraryPrep entity, LibraryPrepDto dto,
                   @Context Set<String> provided, @Context String scope);


  default LibraryPrepBatchDto toDto(LibraryPrepBatch entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toLibraryPrepBatchDto(entity, provided, "libraryPrepBatch");
  }

  @Mapping(target = "libraryPreps", ignore = true)
  @Mapping(target = "indexSet", ignore = true)
  @Mapping(target = "thermocyclerProfile", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnit(), \"storage-unit\"))")
  LibraryPrepBatchDto toLibraryPrepBatchDto(LibraryPrepBatch entity, Set<String> provided, String scope);

}
