package ca.gc.aafc.seqdb.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import ca.gc.aafc.dina.mapper.DinaMapperV2;
import ca.gc.aafc.seqdb.api.dto.SequenceControlledVocabularyDto;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabulary;

import java.util.Set;

@Mapper
public interface SequenceControlledVocabularyMapper extends DinaMapperV2<SequenceControlledVocabularyDto, SequenceControlledVocabulary> {

  SequenceControlledVocabularyMapper INSTANCE = Mappers.getMapper(SequenceControlledVocabularyMapper.class);

  SequenceControlledVocabularyDto toDto(SequenceControlledVocabulary entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  SequenceControlledVocabulary toEntity(SequenceControlledVocabularyDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SequenceControlledVocabulary entity, SequenceControlledVocabularyDto dto, @Context Set<String> provided, @Context String scope);
}
