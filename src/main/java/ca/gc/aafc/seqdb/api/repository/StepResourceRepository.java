package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.StepResourceDto;
import ca.gc.aafc.seqdb.api.entities.workflow.StepResource;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StepResourceRepository extends DinaRepository<StepResourceDto, StepResource> {

  public StepResourceRepository(
    @NonNull DinaService<StepResource> dinaService,
    Optional<DinaAuthorizationService> authService,
    @NonNull BuildProperties props) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(StepResourceDto.class),
      StepResourceDto.class,
      StepResource.class,
      null,
      null,
      props);
  }

}
