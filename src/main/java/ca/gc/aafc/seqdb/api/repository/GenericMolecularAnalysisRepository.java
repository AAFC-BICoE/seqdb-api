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

import ca.gc.aafc.dina.dto.ExternalRelationDto;
import ca.gc.aafc.dina.dto.JsonApiExternalResource;
import ca.gc.aafc.dina.exception.ConflictException;
import ca.gc.aafc.dina.exception.ResourceGoneException;
import ca.gc.aafc.dina.exception.ResourceNotFoundException;
import ca.gc.aafc.dina.exception.ResourcesGoneException;
import ca.gc.aafc.dina.exception.ResourcesNotFoundException;
import ca.gc.aafc.dina.jsonapi.JsonApiBulkDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiBulkResourceIdentifierDocument;
import ca.gc.aafc.dina.jsonapi.JsonApiDocument;
import ca.gc.aafc.dina.mapper.DinaMappingRegistry;
import ca.gc.aafc.dina.repository.DinaRepositoryV2;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.mapper.ExternalRelationshipMapper;
import ca.gc.aafc.seqdb.api.mapper.GenericMolecularAnalysisMapper;
import ca.gc.aafc.seqdb.api.service.GenericMolecularAnalysisService;

import static com.toedter.spring.hateoas.jsonapi.MediaTypes.JSON_API_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;

@RestController
@RequestMapping(value = "${dina.apiPrefix:}", produces = JSON_API_VALUE)
public class GenericMolecularAnalysisRepository extends DinaRepositoryV2<GenericMolecularAnalysisDto, GenericMolecularAnalysis> {

  // Bean does not exist with keycloak disabled.
  private DinaAuthenticatedUser dinaAuthenticatedUser;

  public GenericMolecularAnalysisRepository(
    @NonNull GenericMolecularAnalysisService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      GenericMolecularAnalysisMapper.INSTANCE,
      GenericMolecularAnalysisDto.class,
      GenericMolecularAnalysis.class,
      props, objMapper, new DinaMappingRegistry(GenericMolecularAnalysisDto.class, true));

    this.dinaAuthenticatedUser = dinaAuthenticatedUser.orElse(null);
  }

  @Override
  protected Link generateLinkToResource(GenericMolecularAnalysisDto dto) {
    try {
      return linkTo(methodOn(GenericMolecularAnalysisRepository.class).onFindOne(dto.getUuid(), null)).withSelfRel();
    } catch (ResourceNotFoundException | ResourceGoneException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected JsonApiExternalResource externalRelationDtoToJsonApiExternalResource(ExternalRelationDto externalRelationDto) {
    return ExternalRelationshipMapper.externalRelationDtoToJsonApiExternalResource(externalRelationDto);
  }

  @PostMapping(path = GenericMolecularAnalysisDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_LOAD_PATH, consumes = JSON_API_BULK)
  public ResponseEntity<RepresentationModel<?>> onBulkLoad(@RequestBody JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument,
                                                           HttpServletRequest req)
      throws ResourcesNotFoundException, ResourcesGoneException {
    return handleBulkLoad(jsonApiBulkDocument, req);
  }

  @GetMapping(GenericMolecularAnalysisDto.TYPENAME + "/{id}")
  public ResponseEntity<RepresentationModel<?>> onFindOne(@PathVariable UUID id, HttpServletRequest req)
      throws ResourceNotFoundException, ResourceGoneException {
    return handleFindOne(id, req);
  }

  @GetMapping(GenericMolecularAnalysisDto.TYPENAME)
  public ResponseEntity<RepresentationModel<?>> onFindAll(HttpServletRequest req) {
    return handleFindAll(req);
  }

  @PostMapping(path = GenericMolecularAnalysisDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkCreate(@RequestBody
                                                             JsonApiBulkDocument jsonApiBulkDocument) {
    return handleBulkCreate(jsonApiBulkDocument, dto -> {
      if (dinaAuthenticatedUser != null) {
        dto.setCreatedBy(dinaAuthenticatedUser.getUsername());
      }
    });
  }

  @PostMapping(GenericMolecularAnalysisDto.TYPENAME)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onCreate(@RequestBody JsonApiDocument postedDocument) {
    return handleCreate(postedDocument, dto -> {
      if (dinaAuthenticatedUser != null) {
        dto.setCreatedBy(dinaAuthenticatedUser.getUsername());
      }
    });
  }

  @PatchMapping(GenericMolecularAnalysisDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onUpdate(@RequestBody JsonApiDocument partialPatchDto,
                                                         @PathVariable UUID id)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleUpdate(partialPatchDto, id);
  }

  @PatchMapping(path = GenericMolecularAnalysisDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkUpdate(@RequestBody JsonApiBulkDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException, ConflictException {
    return handleBulkUpdate(jsonApiBulkDocument);
  }

  @DeleteMapping(path = GenericMolecularAnalysisDto.TYPENAME + "/" + DinaRepositoryV2.JSON_API_BULK_PATH, consumes = JSON_API_BULK)
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onBulkDelete(@RequestBody
                                                             JsonApiBulkResourceIdentifierDocument jsonApiBulkDocument)
      throws ResourceNotFoundException, ResourceGoneException {
    return handleBulkDelete(jsonApiBulkDocument);
  }

  @DeleteMapping(GenericMolecularAnalysisDto.TYPENAME + "/{id}")
  @Transactional
  public ResponseEntity<RepresentationModel<?>> onDelete(@PathVariable UUID id) throws ResourceNotFoundException, ResourceGoneException {
    return handleDelete(id);
  }

}
