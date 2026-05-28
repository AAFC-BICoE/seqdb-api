package ca.gc.aafc.seqdb.api.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;

import ca.gc.aafc.dina.dto.JsonApiExternalResource;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents an external relationship of type protocol.
 */
@Builder
@Getter
@JsonApiTypeForClass(ProtocolExternalDto.EXTERNAL_TYPENAME)
public class ProtocolExternalDto implements JsonApiExternalResource {

  public static final String EXTERNAL_TYPENAME = "protocol";

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
