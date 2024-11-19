package ca.gc.aafc.seqdb.api.repository;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.seqdb.api.dto.GenericMolecularAnalysisDto;
import ca.gc.aafc.seqdb.api.entities.GenericMolecularAnalysis;
import ca.gc.aafc.seqdb.api.service.GenericMolecularAnalysisService;

import java.util.Optional;
import lombok.NonNull;

@Repository
public class GenericMolecularAnalysisRepository extends DinaRepository<GenericMolecularAnalysisDto, GenericMolecularAnalysis> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public GenericMolecularAnalysisRepository(
    @NonNull GenericMolecularAnalysisService dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(GenericMolecularAnalysisDto.class),
      GenericMolecularAnalysisDto.class,
      GenericMolecularAnalysis.class,
      null,
      externalResourceProvider,
      props, objMapper);

    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends GenericMolecularAnalysisDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }

}
