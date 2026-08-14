package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.exception.ConflictException;
import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.exception.ResourcesGoneException;
import ca.gc.aafc.dina.exception.ResourcesNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiBulkDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiBulkResourceIdentifierDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.repository.DinaRepositoryV2;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.TextHtmlSanitizer;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.seqdb.api.dto.SequenceManagedAttributeDto;
import ca.gc.aafc.seqdb.api.entities.SequenceManagedAttribute;
import ca.gc.aafc.seqdb.api.mapper.SequenceManagedAttributeMapper;
import ca.gc.aafc.seqdb.api.security.SuperUserInGroupCUDAuthorizationService;
import ca.gc.aafc.seqdb.api.service.SequenceManagedAttributeService;

import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;

@RestController
@RequestMapping(value = "${dina.apiPrefix:}", produces = JSON_API_VALUE)
public class SequenceManagedAttributeRepository extends DinaRepositoryV2<SequenceManagedAttributeDto, SequenceManagedAttribute> {

  private final SequenceManagedAttributeService dinaService;

  // Bean does not exist with keycloak disabled.
  private final DinaAuthenticatedUser dinaAuthenticatedUser;

  public static final Pattern KEY_LOOKUP_PATTERN = Pattern.compile("(.*)\\.(.*)");

  public SequenceManagedAttributeRepository(
    @NonNull SequenceManagedAttributeService service,
    @NonNull SuperUserInGroupCUDAuthorizationService authService,
    @NonNull AuditService auditService,
    @NonNull BuildProperties buildProperties,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objectMapper
  ) {
    super(
      service, authService,
      Optional.of(auditService),
      SequenceManagedAttributeMapper.INSTANCE,
      SequenceManagedAttributeDto.class,
      SequenceManagedAttribute.class,
      buildProperties, objectMapper);
    this.dinaService = service;
    this.dinaAuthenticatedUser = dinaAuthenticatedUser.orElse(null);
  }

  @Override
  protected Link generateLinkToResource(SequenceManagedAttributeDto dto) {
    try {
      return linkTo(methodOn(SequenceManagedAttributeRepository.class).onFindOne(dto.getUuid().toString(), null)).withSelfRel();
    } catch (ResourceNotFoundException | ResourceGoneException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping(SequenceManagedAttributeDto.TYPENAME + "/{id}")
  public ResponseEntity<RepresentationModel<?>> onFindOne(@PathVariable String id, HttpServletRequest req)
      throws ResourceNotFoundException, ResourceGoneException {

    // Allow lookup by component type + key.
    // e.g. collecting_event.attribute_name
    var matcher = KEY_LOOKUP_PATTERN.matcher(id);
    if (matcher.groupCount() == 2) {
      if (matcher.find()) {
        SequenceManagedAttribute.ManagedAttributeComponent component = SequenceManagedAttribute.ManagedAttributeComponent
          .fromString(matcher.group(1));
        String attributeKey = matcher.group(2);

        SequenceManagedAttribute managedAttribute =
          dinaService.findOneByKeyAndComponent(attributeKey, component);

        if (managedAttribute != null) {
          return handleFindOne(managedAttribute.getUuid(), req);
        } else {
          throw ResourceNotFoundException.create(SequenceManagedAttributeDto.TYPENAME,
            TextHtmlSanitizer.sanitizeText(id));
        }
      }
    }
    return handleFindOne(UUID.fromString(id), req);
  }


  @PostMapping(path = SequenceManagedAttributeDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_LOAD_PATH,
    consumes = JSON_API_BULK)
  public ResponseEntity<RepresentationModel<?>> onBulkLoad(@RequestBody
                                                           JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument,
                                                           HttpServletRequest req)
      throws ResourcesNotFoundException, ResourcesGoneException {
    return handleBulkLoad(jsonApiBulkDocument, req);
  }

  @GetMapping(SequenceManagedAttributeDto.TYPENAME)
  public ResponseEntity<RepresentationModel<?>> onFindAll(HttpServletRequest req) {
    return handleFindAll(req);
  }

  @PostMapping(path = SequenceManagedAttributeDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkCreate(@RequestBody
                                                             JsonApiBulkDocument jsonApiBulkDocument) {
    return handleBulkCreate(jsonApiBulkDocument, dto -> {
      if (dinaAuthenticatedUser != null) {
        dto.setCreatedBy(dinaAuthenticatedUser.getUsername());
      }
    });
  }

  @PostMapping(SequenceManagedAttributeDto.TYPENAME)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onCreate(@RequestBody JsonApiDocument postedDocument) {

    return handleCreate(postedDocument, dto -> {
      if (dinaAuthenticatedUser != null) {
        dto.setCreatedBy(dinaAuthenticatedUser.getUsername());
      }
    });
  }

  @PatchMapping(path = SequenceManagedAttributeDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkUpdate(@RequestBody JsonApiBulkDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleBulkUpdate(jsonApiBulkDocument);
  }

  @PatchMapping(SequenceManagedAttributeDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onUpdate(@RequestBody JsonApiDocument partialPatchDto,
                                                         @PathVariable UUID id)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleUpdate(partialPatchDto, id);
  }

  @DeleteMapping(path = SequenceManagedAttributeDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkDelete(@RequestBody
                                                             JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException {
    return handleBulkDelete(jsonApiBulkDocument);
  }

  @DeleteMapping(SequenceManagedAttributeDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onDelete(@PathVariable UUID id) throws ResourceNotFoundException, ResourceGoneException {
    return handleDelete(id);
  }
}
