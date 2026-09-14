package ca.gc.aafc.seqdb.api.dto;

import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.BaseControlledVocabularyDto;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabulary;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@RelatedEntity(SequenceControlledVocabulary.class)
@JsonApiTypeForClass(BaseControlledVocabularyDto.TYPENAME)
@Data
@TypeName(BaseControlledVocabularyDto.TYPENAME)
public class SequenceControlledVocabularyDto extends BaseControlledVocabularyDto {

  @JsonApiId
  @Id
  @PropertyName("id")
  public UUID getUuid() {
    return uuid;
  }
}
