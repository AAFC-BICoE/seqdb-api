package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthenticatedUser;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.SequencingFacilityDto;
import ca.gc.aafc.seqdb.api.entities.SequencingFacility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SequencingFacilityRepository extends DinaRepository<SequencingFacilityDto, SequencingFacility> {

  private Optional<DinaAuthenticatedUser> dinaAuthenticatedUser;

  public SequencingFacilityRepository(
          @NonNull DinaService<SequencingFacility> dinaService,
          DinaAuthorizationService groupAuthorizationService,
          @NonNull BuildProperties props,
          ExternalResourceProvider externalResourceProvider,
          Optional<DinaAuthenticatedUser> dinaAuthenticatedUser,
          ObjectMapper objMapper) {
    super(
            dinaService,
            groupAuthorizationService,
            Optional.empty(),
            new DinaMapper<>(SequencingFacilityDto.class),
            SequencingFacilityDto.class, SequencingFacility.class,
            null,
            externalResourceProvider,
            props, objMapper);
    this.dinaAuthenticatedUser = dinaAuthenticatedUser;
  }

  @Override
  public <S extends SequencingFacilityDto> S create(S resource) {
    dinaAuthenticatedUser.ifPresent(
      authenticatedUser -> resource.setCreatedBy(authenticatedUser.getUsername()));
    return super.create(resource);
  }

}
