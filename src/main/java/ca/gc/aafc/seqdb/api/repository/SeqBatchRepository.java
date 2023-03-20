package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import lombok.NonNull;

@Repository
public class SeqBatchRepository extends DinaRepository<SeqBatchDto, SeqBatch> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public SeqBatchRepository(
    @NonNull DinaService<SeqBatch> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(SeqBatchDto.class),
      SeqBatchDto.class,
      SeqBatch.class,
      null,
      externalResourceProvider,
      props, objMapper);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends SeqBatchDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }

}
