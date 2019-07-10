package ca.gc.aafc.seqdb.api.dto.vocabularies;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data

@JsonApiResource(type = "vocabulary")
public class BaseVocabularyDto {
  
  public BaseVocabularyDto( String id, Object[] enumValues) {
    this.enumType = id;
    this.enumValues = enumValues;
  }
  
  @JsonApiId
  private String enumType;
    
  public Object[] enumValues;
}
