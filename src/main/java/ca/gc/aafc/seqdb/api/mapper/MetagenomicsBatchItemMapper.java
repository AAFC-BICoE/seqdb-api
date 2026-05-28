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
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchDto;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchItemDto;
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatch;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatchItem;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;

@Mapper(imports = MapperStaticConverter.class)
public interface MetagenomicsBatchItemMapper extends DinaMapperV2<MetagenomicsBatchItemDto, MetagenomicsBatchItem> {

  MetagenomicsBatchItemMapper INSTANCE = Mappers.getMapper(MetagenomicsBatchItemMapper.class);

  MetagenomicsBatchItemDto toDto(MetagenomicsBatchItem entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "metagenomicsBatch", ignore = true)
  @Mapping(target = "pcrBatchItem", ignore = true)
  @Mapping(target = "molecularAnalysisRunItem", ignore = true)
  @Mapping(target = "indexI5", ignore = true)
  @Mapping(target = "indexI7", ignore = true)
  MetagenomicsBatchItem toEntity(MetagenomicsBatchItemDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "metagenomicsBatch", ignore = true)
  @Mapping(target = "pcrBatchItem", ignore = true)
  @Mapping(target = "molecularAnalysisRunItem", ignore = true)
  @Mapping(target = "indexI5", ignore = true)
  @Mapping(target = "indexI7", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget MetagenomicsBatchItem entity, MetagenomicsBatchItemDto dto,
                   @Context Set<String> provided, @Context String scope);

  default IndexSetDto toDto(IndexSet entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toIndexSetDto(entity, provided, "indexSet");
  }
  @Mapping(target = "ngsIndexes", ignore = true)
  IndexSetDto toIndexSetDto(IndexSet entity, Set<String> provided, String scope);

  default MetagenomicsBatchDto toDto(MetagenomicsBatch entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toMetagenomicsBatchDto(entity, provided, "metagenomicsBatch");
  }
  @Mapping(target = "indexSet", ignore = true)
  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  MetagenomicsBatchDto toMetagenomicsBatchDto(MetagenomicsBatch entity, Set<String> provided, String scope);

  default PcrBatchItemDto toDto(PcrBatchItem entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toPcrBatchItemDto(entity, provided, "pcrBatchItem");
  }
  @Mapping(target = "pcrBatch", ignore = true)
  @Mapping(target = "materialSample", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getMaterialSample(), \"material-sample\"))")
  @Mapping(target = "storageUnitUsage", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnitUsage(), \"storage-unit-usage\"))")
  PcrBatchItemDto toPcrBatchItemDto(PcrBatchItem entity, Set<String> provided, String scope);

  default MolecularAnalysisRunItemDto toDto(MolecularAnalysisRunItem entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toMolecularAnalysisRunItemDto(entity, provided, "molecularAnalysisRunItem");
  }
  @Mapping(target = "run", ignore = true)
  @Mapping(target = "result", ignore = true)
  MolecularAnalysisRunItemDto toMolecularAnalysisRunItemDto(MolecularAnalysisRunItem entity, Set<String> provided, String scope);

}
