package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.auth.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ThermocyclerProfileDto;
import ca.gc.aafc.seqdb.api.entities.ThermocyclerProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ThermocyclerProfileRepository extends DinaRepository<ThermocyclerProfileDto, ThermocyclerProfile> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public ThermocyclerProfileRepository(
          @NonNull DinaService<ThermocyclerProfile> dinaService,
          DinaAuthorizationService groupAuthorizationService,
          @NonNull BuildProperties props,
          ExternalResourceProvider externalResourceProvider,
          Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
          ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(ThermocyclerProfileDto.class),
      ThermocyclerProfileDto.class,
      ThermocyclerProfile.class,
      null,
      externalResourceProvider,
      props, objMapper);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends ThermocyclerProfileDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }

}
