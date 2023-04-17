package ca.gc.aafc.seqdb.api;

import ca.gc.aafc.dina.property.YamlPropertyLoaderFactory;
import ca.gc.aafc.dina.vocabulary.VocabularyConfiguration;
import ca.gc.aafc.dina.vocabulary.VocabularyElementConfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Configuration
@PropertySource(value = "classpath:vocabulary/pcrBatchType.yml", factory = YamlPropertyLoaderFactory.class)
@ConfigurationProperties
@Validated
public class SequenceVocabularyConfiguration extends VocabularyConfiguration<VocabularyElementConfiguration > {

  public static final String PCR_BATCH_TYPE_VOCAB_KEY = "pcrBatchType";

  public SequenceVocabularyConfiguration(Map<String, List<VocabularyElementConfiguration>> vocabulary) {
    super(vocabulary);
  }

  public List<VocabularyElementConfiguration> getVocabularyByKey(String key) {
    return getVocabulary().get(key);
  }
}
