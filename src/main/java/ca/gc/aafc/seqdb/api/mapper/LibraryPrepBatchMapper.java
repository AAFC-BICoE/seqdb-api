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
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;

import java.util.Set;

@Mapper(imports = MapperStaticConverter.class)
public interface LibraryPrepBatchMapper extends DinaMapperV2<LibraryPrepBatchDto, LibraryPrepBatch> {

  LibraryPrepBatchMapper INSTANCE = Mappers.getMapper(LibraryPrepBatchMapper.class);

  @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnit(), \"storage-unit\"))")
  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  LibraryPrepBatchDto toDto(LibraryPrepBatch entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPreps", ignore = true)
  @Mapping(target = "indexSet", ignore = true)
  @Mapping(target = "thermocyclerProfile", ignore = true)
  @Mapping(target = "product", ignore = true)
 // @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(dto.getProtocol(), \"protocol\"))")
 // @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(dto.getStorageUnit(), \"storage-unit\"))")
  LibraryPrepBatch toEntity(LibraryPrepBatchDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPreps", ignore = true)
  @Mapping(target = "indexSet", ignore = true)
  @Mapping(target = "thermocyclerProfile", ignore = true)
  @Mapping(target = "product", ignore = true)
 // @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
 // @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnit(), \"storage-unit\"))")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget LibraryPrepBatch entity, LibraryPrepBatchDto dto,
                   @Context Set<String> provided, @Context String scope);

  default LibraryPrepDto toDto(LibraryPrep entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toLibraryPrepDto(entity, provided, "libraryPreps");
  }

  @Mapping(target = "libraryPrepBatch", ignore = true)
  @Mapping(target = "storageUnitUsage", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnitUsage(), \"storage-unit-usage\"))")
  @Mapping(target = "materialSample", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getMaterialSample(), \"material-sample\"))")
  LibraryPrepDto toLibraryPrepDto(LibraryPrep entity, Set<String> provided, String scope);
}
