package ca.gc.aafc.seqdb.api;

import ca.gc.aafc.dina.property.YamlPropertyLoaderFactory;
import ca.gc.aafc.dina.vocabulary.VocabularyConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@Configuration
@PropertySource(value = "classpath:vocabulary/pcrType.yml", factory = YamlPropertyLoaderFactory.class)
@ConfigurationProperties
public class SequenceVocabularyConfiguration extends VocabularyConfiguration<VocabularyConfiguration.VocabularyElement> {

  public SequenceVocabularyConfiguration(Map<String, List<VocabularyElement>> vocabulary) {
    super(vocabulary);
  }
}
