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
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.PreLibraryPrep;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;

@Mapper(imports = MapperStaticConverter.class)
public interface PreLibraryPrepMapper extends DinaMapperV2<PreLibraryPrepDto, PreLibraryPrep> {

  PreLibraryPrepMapper INSTANCE = Mappers.getMapper(PreLibraryPrepMapper.class);

  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  PreLibraryPrepDto toDto(PreLibraryPrep entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPrep", ignore = true)
  @Mapping(target = "product", ignore = true)
  PreLibraryPrep toEntity(PreLibraryPrepDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "libraryPrep", ignore = true)
  @Mapping(target = "product", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget PreLibraryPrep entity, PreLibraryPrepDto dto,
                   @Context Set<String> provided, @Context String scope);


  default LibraryPrepDto toDto(LibraryPrep entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toLibraryPrepDto(entity, provided, "libraryPrep");
  }

  @Mapping(target = "libraryPrepBatch", ignore = true)
  @Mapping(target = "storageUnitUsage", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnitUsage(), \"storage-unit-usage\"))")
  @Mapping(target = "materialSample", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getMaterialSample(), \"material-sample\"))")
  LibraryPrepDto toLibraryPrepDto(LibraryPrep entity, Set<String> provided, String scope);
}

