package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.TextHtmlSanitizer;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.seqdb.api.dto.SequenceManagedAttributeDto;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;
import ca.gc.aafc.seqdb.api.security.SuperUserInGroupCUDAuthorizationService;
import ca.gc.aafc.seqdb.api.service.SequenceManagedAttributeService;

import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import java.io.Serializable;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.NonNull;

@Repository
public class SequenceManagedAttributeRepository extends DinaRepository<SequenceManagedAttributeDto, SequenceManagedAttribute> {

  private final SequenceManagedAttributeService dinaService;
  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public static final Pattern KEY_LOOKUP_PATTERN = Pattern.compile("(.*)\\.(.*)");

  public SequenceManagedAttributeRepository(
    @NonNull SequenceManagedAttributeService service,
    @NonNull SuperUserInGroupCUDAuthorizationService authService,
    ExternalResourceProvider externalResourceProvider,
    @NonNull AuditService auditService,
    @NonNull BuildProperties buildProperties,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objectMapper
  ) {
    super(
      service, authService,
      Optional.of(auditService),
      new DinaMapper<>(SequenceManagedAttributeDto.class),
      SequenceManagedAttributeDto.class,
      SequenceManagedAttribute.class,
      null,
      externalResourceProvider,
      buildProperties, objectMapper);
    this.dinaService = service;
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends SequenceManagedAttributeDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }

  @Override
  public SequenceManagedAttributeDto findOne(Serializable id, QuerySpec querySpec) {

    // Allow lookup by component type + key.
    // e.g. collecting_event.attribute_name
    var matcher = KEY_LOOKUP_PATTERN.matcher(id.toString());
    if (matcher.groupCount() == 2) {
      if (matcher.find()) {
        SequenceManagedAttribute.ManagedAttributeComponent component = SequenceManagedAttribute.ManagedAttributeComponent
          .fromString(matcher.group(1));
        String attributeKey = matcher.group(2);

        SequenceManagedAttribute managedAttribute =
          dinaService.findOneByKeyAndComponent(attributeKey, component);

        if (managedAttribute != null) {
          return getMappingLayer().toDtoSimpleMapping(managedAttribute);
        } else {
          throw new ResourceNotFoundException("Managed Attribute not found: " + TextHtmlSanitizer.sanitizeText(id.toString()));
        }
      }
    }

    return super.findOne(id, querySpec);
  }
}
