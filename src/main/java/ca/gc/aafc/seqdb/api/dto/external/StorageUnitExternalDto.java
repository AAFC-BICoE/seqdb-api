package ca.gc.aafc.seqdb.api.dto.external;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.JsonApiExternalResource;

/**
 * Represents an external relationship of type storage-unit.
 */
@Builder
@Getter
@JsonApiTypeForClass(StorageUnitUsageExternalDto.EXTERNAL_TYPENAME)
public class StorageUnitExternalDto implements JsonApiExternalResource {

  public static final String EXTERNAL_TYPENAME = "storage-unit";

  @JsonApiId
  private UUID uuid;

  @JsonIgnore
  @Override
  public String getJsonApiType() {
    return EXTERNAL_TYPENAME;
  }

  @JsonIgnore
  @Override
  public UUID getJsonApiId() {
    return uuid;
  }
}

