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
import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;
import ca.gc.aafc.seqdb.api.entities.SeqSubmission;

@Mapper
public interface SeqSubmissionMapper extends DinaMapperV2<SeqSubmissionDto, SeqSubmission> {

  SeqSubmissionMapper INSTANCE = Mappers.getMapper(SeqSubmissionMapper.class);

  SeqSubmissionDto toDto(SeqSubmission entity, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  SeqSubmission toEntity(SeqSubmissionDto dto, @Context Set<String> provided, @Context String scope);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void patchEntity(@MappingTarget SeqSubmission entity, SeqSubmissionDto dto,
                   @Context Set<String> provided, @Context String scope);
}
