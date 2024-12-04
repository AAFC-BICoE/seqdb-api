package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchDto;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatch;
import ca.gc.aafc.seqdb.api.service.MetagenomicsBatchService;

import java.util.Optional;
import lombok.NonNull;

@Repository
public class MetagenomicsBatchRepository extends DinaRepository<MetagenomicsBatchDto, MetagenomicsBatch> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public MetagenomicsBatchRepository(
    @NonNull MetagenomicsBatchService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(MetagenomicsBatchDto.class),
      MetagenomicsBatchDto.class,
      MetagenomicsBatch.class,
      null,
      externalResourceProvider,
      props, objMapper);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends MetagenomicsBatchDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }
}
