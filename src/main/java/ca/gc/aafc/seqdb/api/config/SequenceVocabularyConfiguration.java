package ca.gc.aafc.seqdb.api.config;

import java.util.UUID;

public final class SequenceVocabularyConfiguration {

  // Constant, by Liquibase migration
  public static final UUID MANAGED_ATTRIBUTE_VOCAB_UUID = UUID.fromString("fe62b8b4-875d-43b4-91a8-94f85b3aae42");

  private SequenceVocabularyConfiguration() {
    // no-op
  }

  public enum DinaComponent {
    GENERIC_MOLECULAR_ANALYSIS;

    public static DinaComponent fromString(String s) {
      for (DinaComponent source : DinaComponent.values()) {
        if (source.name().equalsIgnoreCase(s)) {
          return source;
        }
      }
      return null;
    }
  }
}
