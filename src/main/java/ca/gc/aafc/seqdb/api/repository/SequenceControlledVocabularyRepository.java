package ca.gc.aafc.seqdb.api.repository;

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
import ca.gc.aafc.dina.security.auth.DinaAdminCUDAuthorizationService;
import ca.gc.aafc.dina.service.AuditService;
import ca.gc.aafc.dina.util.UUIDHelper;
import ca.gc.aafc.seqdb.api.dto.SequenceControlledVocabularyDto;
import ca.gc.aafc.seqdb.api.entities.SequenceControlledVocabulary;
import ca.gc.aafc.seqdb.api.mapper.SequenceControlledVocabularyMapper;
import ca.gc.aafc.seqdb.api.service.SequenceControlledVocabularyService;

import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;

@RestController
@RequestMapping(value = "${dina.apiPrefix:}", produces = JSON_API_VALUE)
public class SequenceControlledVocabularyRepository extends DinaRepositoryV2<SequenceControlledVocabularyDto, SequenceControlledVocabulary> {

  // Bean does not exist with keycloak disabled.
  private final DinaAuthenticatedUser authenticatedUser;

  private final SequenceControlledVocabularyService sequenceControlledVocabularyService;

  public SequenceControlledVocabularyRepository(
    @NonNull SequenceControlledVocabularyService dinaService,
    @NonNull DinaAdminCUDAuthorizationService authorizationService,
    Optional<DinaAuthenticatedUser> authenticatedUser,
    @NonNull BuildProperties props,
    @NonNull AuditService auditService,
    @NonNull ObjectMapper objMapper
  ) {
    super(
      dinaService, authorizationService,
      Optional.of(auditService),
      SequenceControlledVocabularyMapper.INSTANCE,
      SequenceControlledVocabularyDto.class,
      SequenceControlledVocabulary.class,
      props, objMapper);
    this.authenticatedUser = authenticatedUser.orElse(null);
    this.sequenceControlledVocabularyService = dinaService;
  }

  @Override
  protected Link generateLinkToResource(SequenceControlledVocabularyDto dto) {
    try {
      return linkTo(methodOn(SequenceControlledVocabularyRepository.class).onFindOne(dto.getUuid().toString(), null)).withSelfRel();
    } catch (ResourceNotFoundException | ResourceGoneException e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping(SequenceControlledVocabularyDto.TYPENAME + "/{idOrKey}")
  public ResponseEntity<RepresentationModel<?>> onFindOne(@PathVariable String idOrKey, HttpServletRequest req)
      throws ResourceNotFoundException, ResourceGoneException {

    if (StringUtils.isBlank(idOrKey)) {
      throw ResourceNotFoundException.create(SequenceControlledVocabularyDto.TYPENAME, "");
    }

    Optional<UUID> id = UUIDHelper.toUUID(idOrKey);
    if (id.isPresent()) {
      return handleFindOne(id.get(), req);
    }

    SequenceControlledVocabulary entity = sequenceControlledVocabularyService.findOneByKey(idOrKey);
    if (entity == null) {
      throw ResourceNotFoundException.create(SequenceControlledVocabularyDto.TYPENAME, idOrKey);
    }
    return handleFindOne(entity.getUuid(), req);
  }

  @PostMapping(path = SequenceControlledVocabularyDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_LOAD_PATH,
    consumes = JSON_API_BULK)
  public ResponseEntity<RepresentationModel<?>> onBulkLoad(@RequestBody
                                                           JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument,
                                                           HttpServletRequest req)
      throws ResourcesNotFoundException, ResourcesGoneException {
    return handleBulkLoad(jsonApiBulkDocument, req);
  }

  @GetMapping(SequenceControlledVocabularyDto.TYPENAME)
  public ResponseEntity<RepresentationModel<?>> onFindAll(HttpServletRequest req) {
    return handleFindAll(req);
  }

  @PostMapping(path = SequenceControlledVocabularyDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkCreate(@RequestBody
                                                             JsonApiBulkDocument jsonApiBulkDocument) {
    return handleBulkCreate(jsonApiBulkDocument, dto -> {
      if (authenticatedUser != null) {
        dto.setCreatedBy(authenticatedUser.getUsername());
      }
    });
  }

  @PostMapping(SequenceControlledVocabularyDto.TYPENAME)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onCreate(@RequestBody JsonApiDocument postedDocument) {

    return handleCreate(postedDocument, dto -> {
      if (authenticatedUser != null) {
        dto.setCreatedBy(authenticatedUser.getUsername());
      }
    });
  }

  @PatchMapping(path = SequenceControlledVocabularyDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkUpdate(@RequestBody JsonApiBulkDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleBulkUpdate(jsonApiBulkDocument);
  }

  @PatchMapping(SequenceControlledVocabularyDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onUpdate(@RequestBody JsonApiDocument partialPatchDto,
                                                         @PathVariable UUID id)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleUpdate(partialPatchDto, id);
  }

  @DeleteMapping(path = SequenceControlledVocabularyDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkDelete(@RequestBody
                                                             JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException {
    return handleBulkDelete(jsonApiBulkDocument);
  }

  @DeleteMapping(SequenceControlledVocabularyDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onDelete(@PathVariable UUID id) throws ResourceNotFoundException, ResourceGoneException {
    return handleDelete(id);
  }
}
