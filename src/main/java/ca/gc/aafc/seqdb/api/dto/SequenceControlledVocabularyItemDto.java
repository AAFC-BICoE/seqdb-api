package ca.gc.aafc.seqdb.api.dto;

import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.ShallowReference;
import org.javers.core.metamodel.annotation.TypeName;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.BaseControlledVocabularyItemDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@RelatedEntity(SequenceControlledVocabularyItem.class)
@JsonApiTypeForClass(BaseControlledVocabularyItemDto.TYPENAME)
@Data
@TypeName(BaseControlledVocabularyItemDto.TYPENAME)
public class SequenceControlledVocabularyItemDto extends BaseControlledVocabularyItemDto<SequenceControlledVocabularyDto> {

  private SequenceControlledVocabularyDto controlledVocabulary;

  @JsonApiId
  @Id
  @PropertyName("id")
  public UUID getUuid() {
    return uuid;
  }

  @Override
  @JsonIgnore
  @ShallowReference
  public SequenceControlledVocabularyDto getControlledVocabulary() {
    return controlledVocabulary;
  }
}
