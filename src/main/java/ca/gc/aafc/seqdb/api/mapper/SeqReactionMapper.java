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
import ca.gc.aafc.seqdb.api.dto.SeqReactionDto;
import ca.gc.aafc.seqdb.api.entities.SeqReaction;

@Mapper
public interface SeqReactionMapper extends DinaMapperV2<SeqReactionDto, SeqReaction> {

  SeqReactionMapper INSTANCE = Mappers.getMapper(SeqReactionMapper.class);

  SeqReactionDto toDto(SeqReaction entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  SeqReaction toEntity(SeqReactionDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SeqReaction entity, SeqReactionDto dto,
                   @Context Set<String> provided, @Context String scope);
}
