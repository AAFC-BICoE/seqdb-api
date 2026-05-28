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
import ca.gc.aafc.seqdb.api.dto.MolecularAnalysisRunItemDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRunItem;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;

@Mapper(imports = MapperStaticConverter.class)
public interface SeqReactionMapper extends DinaMapperV2<SeqReactionDto, SeqReaction> {

  SeqReactionMapper INSTANCE = Mappers.getMapper(SeqReactionMapper.class);

  @Mapping(target = "storageUnitUsage", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnitUsage(), \"storage-unit-usage\"))")
  SeqReactionDto toDto(SeqReaction entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "seqBatch", ignore = true)
  @Mapping(target = "pcrBatchItem", ignore = true)
  @Mapping(target = "seqPrimer", ignore = true)
  @Mapping(target = "molecularAnalysisRunItem", ignore = true)
  SeqReaction toEntity(SeqReactionDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "seqBatch", ignore = true)
  @Mapping(target = "pcrBatchItem", ignore = true)
  @Mapping(target = "seqPrimer", ignore = true)
  @Mapping(target = "molecularAnalysisRunItem", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SeqReaction entity, SeqReactionDto dto,
                   @Context Set<String> provided, @Context String scope);


  default SeqBatchDto toDto(SeqBatch entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toSeqBatchDto(entity, provided, "seqBatch");
  }
  @Mapping(target = "protocol", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getProtocol(), \"protocol\"))")
  @Mapping(target = "storageUnit", expression = "java(MapperStaticConverter.uuidToExternalRelation(entity.getStorageUnit(), \"storage-unit\"))")
  @Mapping(target = "experimenters", expression = "java(MapperStaticConverter.uuidListToExternalRelationsList(entity.getExperimenters(), \"person\"))")
  SeqBatchDto toSeqBatchDto(SeqBatch entity, Set<String> provided, String scope);

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

  default PcrPrimerDto toDto(PcrPrimer entity, @Context Set<String> provided, @Context String scope) {
    return entity == null ? null : toPcrPrimerDto(entity, provided, "seqPrimer");
  }
  @Mapping(target = "region", ignore = true)
  PcrPrimerDto toPcrPrimerDto(PcrPrimer entity, Set<String> provided, String scope);

}
