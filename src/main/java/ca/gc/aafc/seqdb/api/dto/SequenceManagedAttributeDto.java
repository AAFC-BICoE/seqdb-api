package ca.gc.aafc.seqdb.api.dto;

import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.i18n.MultilingualDescription;
import ca.gc.aafc.dina.vocabulary.TypedVocabularyElement;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.javers.core.metamodel.annotation.Id;
import org.javers.core.metamodel.annotation.PropertyName;
import org.javers.core.metamodel.annotation.TypeName;

@RelatedEntity(SequenceManagedAttribute.class)
@Data
@JsonApiResource(type = SequenceManagedAttributeDto.TYPENAME)
@TypeName(SequenceManagedAttributeDto.TYPENAME)
public class SequenceManagedAttributeDto {
  public static final String TYPENAME = "managed-attribute";

  @JsonApiId
  @Id
  @PropertyName("id")
  private UUID uuid;
  private String name;
  private String key;
  private TypedVocabularyElement.VocabularyElementType vocabularyElementType;
  private String unit;
  private SequenceManagedAttribute.ManagedAttributeComponent managedAttributeComponent;
  private String[] acceptedValues;
  private OffsetDateTime createdOn;
  private String createdBy;
  private String group;
  private MultilingualDescription multilingualDescription;

}
