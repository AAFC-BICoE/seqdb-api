package ca.gc.aafc.seqdb.api.mapper;

import java.util.UUID;

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiExternalResource;
import ca.gc.aafc.seqdb.api.dto.external.MaterialSampleExternalDto;
import ca.gc.aafc.seqdb.api.dto.external.MetadataExternalDto;
import ca.gc.aafc.seqdb.api.dto.external.PersonExternalDto;
import ca.gc.aafc.seqdb.api.dto.external.ProtocolExternalDto;
import ca.gc.aafc.seqdb.api.dto.external.StorageUnitExternalDto;
import ca.gc.aafc.seqdb.api.dto.external.StorageUnitUsageExternalDto;

/**
 * Not a MapStruct mapper
 *
 * Map known external Relationship types
 */
public final class ExternalRelationshipMapper {
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
      case MaterialSampleExternalDto.EXTERNAL_TYPENAME -> MaterialSampleExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      case StorageUnitUsageExternalDto.EXTERNAL_TYPENAME -> StorageUnitUsageExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      case StorageUnitExternalDto.EXTERNAL_TYPENAME -> StorageUnitExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      case MetadataExternalDto.EXTERNAL_TYPENAME -> MetadataExternalDto.builder().uuid(UUID.fromString(externalRelationDto.getId())).build();
      default -> throw new IllegalStateException("Unsupported type for JsonApiExternalResource: " + externalRelationDto.getType());
    };
  }
}
