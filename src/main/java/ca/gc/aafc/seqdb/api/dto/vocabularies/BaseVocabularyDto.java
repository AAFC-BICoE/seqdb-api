package ca.gc.aafc.seqdb.api.dto.vocabularies;

import java.util.HashSet;
import java.util.Set;

import io.crnk.core.resource.annotations.JsonApiId;
import io.crnk.core.resource.annotations.JsonApiResource;
import lombok.Data;

@Data

@JsonApiResource(type = "baseVocabulary", resourcePath ="vocabulary")
public class BaseVocabularyDto {
  
  @JsonApiId
  private String enumType;
    
  public Object[] enumKeys;
  
  public Set<String> keyStrings = new HashSet<String>();
  
  public boolean addKeyString(String key) {
    return this.keyStrings.add(key);
  }
 

  
  
  
}