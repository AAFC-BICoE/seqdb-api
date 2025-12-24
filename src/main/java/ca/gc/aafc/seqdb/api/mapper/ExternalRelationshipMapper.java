package ca.gc.aafc.seqdb.api.mapper;

import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiExternalResource;
import ca.gc.aafc.seqdb.api.dto.external.PersonExternalDto;
import ca.gc.aafc.seqdb.api.dto.external.ProtocolExternalDto;

/**
 * Not a MapStruct mapper
 *
 * Map known external Relationship types
 */
public class ExternalRelationshipMapper {
  private ExternalRelationshipMapper() {
    //utility class
  }

  public static JsonApiExternalResource externalRelationDtoToJsonApiExternalResource(ExternalRelationDto externalRelationDto) {
    if (externalRelationDto == null) {
      return null;
    }

    return switch (externalRelationDto.getType()) {
      case PersonExternalDto.EXTERNAL_TYPENAME -> PersonExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      case ProtocolExternalDto.EXTERNAL_TYPENAME -> ProtocolExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      default -> throw new IllegalStateException("Unsupported type for JsonApiExternalResource: " + externalRelationDto.getType());
    };
  }
}
