package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.MetagenomicsBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.MetagenomicsBatchItem;
import ca.gc.aafc.seqdb.api.service.MetagenomicsBatchItemService;

import java.util.Optional;
import lombok.NonNull;

@Repository
public class MetagenomicsBatchItemRepository extends DinaRepository<MetagenomicsBatchItemDto, MetagenomicsBatchItem> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public MetagenomicsBatchItemRepository(
    @NonNull MetagenomicsBatchItemService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(MetagenomicsBatchItemDto.class),
      MetagenomicsBatchItemDto.class,
      MetagenomicsBatchItem.class,
      null,
      externalResourceProvider,
      props, objMapper);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends MetagenomicsBatchItemDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }
}