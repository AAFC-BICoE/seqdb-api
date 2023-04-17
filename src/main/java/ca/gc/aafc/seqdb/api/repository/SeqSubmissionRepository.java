package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.SeqSubmissionDto;
import ca.gc.aafc.seqdb.api.entities.SeqSubmission;
import ca.gc.aafc.seqdb.api.service.SeqSubmissionService;

import java.util.Optional;
import lombok.NonNull;

@Repository
public class SeqSubmissionRepository extends DinaRepository<SeqSubmissionDto, SeqSubmission> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public SeqSubmissionRepository(
    SeqSubmissionService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(SeqSubmissionDto.class),
      SeqSubmissionDto.class, SeqSubmission.class,
      null,
      externalResourceProvider,
      props, objMapper);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends SeqSubmissionDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }

}
