package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;
import lombok.NonNull;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisItemDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysisItem;
import ca.gc.aafc.seqdb.api.service.GenericMolecularAnalysisItemService;

@Repository
public class GenericMolecularAnalysisItemRepository extends DinaRepository<GenericMolecularAnalysisItemDto, GenericMolecularAnalysisItem> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public GenericMolecularAnalysisItemRepository(
    @NonNull GenericMolecularAnalysisItemService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(GenericMolecularAnalysisItemDto.class),
      GenericMolecularAnalysisItemDto.class,
      GenericMolecularAnalysisItem.class,
      null,
      externalResourceProvider,
      props, objMapper);

    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends GenericMolecularAnalysisItemDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }

}
