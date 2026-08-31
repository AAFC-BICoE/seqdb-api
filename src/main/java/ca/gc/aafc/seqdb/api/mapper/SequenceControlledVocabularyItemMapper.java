package ca.gc.aafc.seqdb.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.seqdb.api.dto.SequenceControlledVocabularyItemDto;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;

import java.util.Set;

@Mapper
public interface SequenceControlledVocabularyItemMapper extends DinaMapperV2<SequenceControlledVocabularyItemDto, SequenceControlledVocabularyItem> {

  SequenceControlledVocabularyItemMapper INSTANCE = Mappers.getMapper(SequenceControlledVocabularyItemMapper.class);

  SequenceControlledVocabularyItemDto toDto(SequenceControlledVocabularyItem entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "controlledVocabulary", ignore = true)
  SequenceControlledVocabularyItem toEntity(SequenceControlledVocabularyItemDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "controlledVocabulary", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SequenceControlledVocabularyItem entity, SequenceControlledVocabularyItemDto dto, @Context Set<String> provided, @Context String scope);
}
