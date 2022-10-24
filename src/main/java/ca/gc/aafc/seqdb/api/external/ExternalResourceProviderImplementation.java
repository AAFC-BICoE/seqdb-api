package ca.gc.aafc.seqdb.api.external; 

import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class ExternalResourceProviderImplementation implements ExternalResourceProvider {

  public static final Map<String, String> TYPE_TO_REFERENCE_MAP = Map.of(
    "material-sample", "collection/api/v1/material-sample",
    "metadata", "objectstore/api/v1/metadata",
    "person", "agent/api/v1/person",
    "protocol", "collection/api/v1/protocol",
    "storage-unit", "collection/api/v1/storage-unit",
    "storage-unit-type", "collection/api/v1/storage-unit-type"
  );

  @Override
  public String getReferenceForType(String type) {
    return TYPE_TO_REFERENCE_MAP.get(type);
  }

  @Override
  public Set<String> getTypes() {
    return TYPE_TO_REFERENCE_MAP.keySet();
  }
}
