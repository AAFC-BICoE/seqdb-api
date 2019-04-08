package ca.gc.aafc.seqdb.api.dto.vocabularies;

import java.util.Map;
import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data

@JsonApiResource(type = "baseVocabulary", resourcePath ="vocabulary")
public class BaseVocabularyDto {
  
  public BaseVocabularyDto(Map<String,Object[]> enumMap) {
    this.enumType = "baseEnum";
    this.enumMap = enumMap;
  }
  @JsonApiId
  private String enumType;
    
  public Map<String, Object[]> enumMap;
 

  
  
  
}