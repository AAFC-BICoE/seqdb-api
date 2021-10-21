package ca.gc.aafc.seqdb.api;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import ca.gc.aafc.dina.vocabulary.VocabularyConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
public class SeqdbVocabularyConfiguration extends VocabularyConfiguration<SeqdbVocabularyConfiguration.SeqdbVocabularyElement> {
  
  public SeqdbVocabularyConfiguration(Map<String, List<SeqdbVocabularyElement>> vocabulary) {
    super(vocabulary);
  }

  @NoArgsConstructor
  @Getter
  @Setter
  public static class SeqdbVocabularyElement extends VocabularyConfiguration.VocabularyElement {
    private String inverseOf;
  }
}
