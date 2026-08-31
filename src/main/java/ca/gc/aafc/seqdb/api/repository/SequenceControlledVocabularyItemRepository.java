package ca.gc.aafc.seqdb.api.repository;

import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
import ca.gc.aafc.dina.security.auth.SuperUserInGroupCUDAuthorizationService;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.dina.util.UUIDHelper;
import ca.gc.aafc.seqdb.api.dto.SequenceControlledVocabularyItemDto;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabulary;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabularyItem;
import ca.gc.aafc.seqdb.api.mapper.SequenceControlledVocabularyItemMapper;
import ca.gc.aafc.seqdb.api.service.SequenceControlledVocabularyItemService;
import ca.gc.aafc.seqdb.api.service.SequenceControlledVocabularyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;

@RestController
@RequestMapping(value = "${dina.apiPrefix:}", produces = JSON_API_VALUE)
public class SequenceControlledVocabularyItemRepository extends DinaRepositoryV2<SequenceControlledVocabularyItemDto, SequenceControlledVocabularyItem> {

  // Bean does not exist with keycloak disabled.
  private final DinaAuthenticatedUser authenticatedUser;

  private final SequenceControlledVocabularyService controlledVocabularyService;
  private final SequenceControlledVocabularyItemService controlledVocabularyItemService;

  public SequenceControlledVocabularyItemRepository(
    @NonNull SequenceControlledVocabularyItemService controlledVocabularyItemService,
    @NonNull SequenceControlledVocabularyService controlledVocabularyService,
    @NonNull SuperUserInGroupCUDAuthorizationService authorizationService,
    Optional<DinaAuthenticatedUser> authenticatedUser,
    @NonNull BuildProperties props,
    @NonNull AuditService auditService,
    @NonNull ObjectMapper objMapper
  ) {
    super(
      controlledVocabularyItemService, authorizationService,
      Optional.of(auditService),
      SequenceControlledVocabularyItemMapper.INSTANCE,
      SequenceControlledVocabularyItemDto.class,
      SequenceControlledVocabularyItem.class,
      props, objMapper);
    this.authenticatedUser = authenticatedUser.orElse(null);
    this.controlledVocabularyItemService = controlledVocabularyItemService;
    this.controlledVocabularyService = controlledVocabularyService;
  }

  @Override
  protected Link generateLinkToResource(SequenceControlledVocabularyItemDto dto) {
    try {
      return linkTo(methodOn(SequenceControlledVocabularyItemRepository.class).onFindOne(dto.getUuid().toString(), null)).withSelfRel();
    } catch (ResourceNotFoundException | ResourceGoneException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping(SequenceControlledVocabularyItemDto.TYPENAME + "/{idOrKey}")
  public ResponseEntity<RepresentationModel<?>> onFindOne(@PathVariable String idOrKey, HttpServletRequest req)
      throws ResourceNotFoundException, ResourceGoneException {

    if (StringUtils.isBlank(idOrKey)) {
      throw ResourceNotFoundException.create(SequenceControlledVocabularyItemDto.TYPENAME, "");
    }

    Optional<UUID> id = UUIDHelper.toUUID(idOrKey);
    if (id.isPresent()) {
      return handleFindOne(id.get(), req);
    }


    // key is always a compound key vocabKey.itemKey[.dinaComponent]
    String[] keyParts = StringUtils.split(idOrKey, ".");
    if (keyParts.length == 2 || keyParts.length == 3) {
      SequenceControlledVocabulary vocab = controlledVocabularyService.findOneByKey(keyParts[0]);
      if (vocab != null) {
        SequenceControlledVocabularyItem item = controlledVocabularyItemService.findOneByKey(keyParts[1], vocab.getUuid(),
          keyParts.length == 3 ? keyParts[2] : null);
        if (item != null) {
          return handleFindOne(item.getUuid(), req);
        }
      }
    }
    throw ResourceNotFoundException.create(SequenceControlledVocabularyItemDto.TYPENAME,
      TextHtmlSanitizer.sanitizeText(idOrKey));
  }

  @PostMapping(path = SequenceControlledVocabularyItemDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_LOAD_PATH,
    consumes = JSON_API_BULK)
  public ResponseEntity<RepresentationModel<?>> onBulkLoad(@RequestBody
                                                           JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument,
                                                           HttpServletRequest req)
      throws ResourcesNotFoundException, ResourcesGoneException {
    return handleBulkLoad(jsonApiBulkDocument, req);
  }

  @GetMapping(SequenceControlledVocabularyItemDto.TYPENAME)
  public ResponseEntity<RepresentationModel<?>> onFindAll(HttpServletRequest req) {
    return handleFindAll(req);
  }

  @PostMapping(path = SequenceControlledVocabularyItemDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkCreate(@RequestBody
                                                             JsonApiBulkDocument jsonApiBulkDocument) {
    return handleBulkCreate(jsonApiBulkDocument, dto -> {
      if (authenticatedUser != null) {
        dto.setCreatedBy(authenticatedUser.getUsername());
      }
    });
  }

  @PostMapping(SequenceControlledVocabularyItemDto.TYPENAME)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onCreate(@RequestBody JsonApiDocument postedDocument) {

    return handleCreate(postedDocument, dto -> {
      if (authenticatedUser != null) {
        dto.setCreatedBy(authenticatedUser.getUsername());
      }
    });
  }

  @PatchMapping(path = SequenceControlledVocabularyItemDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkUpdate(@RequestBody JsonApiBulkDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleBulkUpdate(jsonApiBulkDocument);
  }

  @PatchMapping(SequenceControlledVocabularyItemDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onUpdate(@RequestBody JsonApiDocument partialPatchDto,
                                                         @PathVariable UUID id)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleUpdate(partialPatchDto, id);
  }

  @DeleteMapping(path = SequenceControlledVocabularyItemDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkDelete(@RequestBody
                                                             JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException {
    return handleBulkDelete(jsonApiBulkDocument);
  }

  @DeleteMapping(SequenceControlledVocabularyItemDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onDelete(@PathVariable UUID id) throws ResourceNotFoundException, ResourceGoneException {
    return handleDelete(id);
  }
}
