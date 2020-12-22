package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ContainerTypeDto;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import lombok.NonNull;

@Repository
public class ContainerTypeRepository extends DinaRepository<ContainerTypeDto, ContainerType> {

  public ContainerTypeRepository(
    @NonNull DinaService<ContainerType> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService,
    @NonNull BuildProperties props) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(ContainerTypeDto.class),
      ContainerTypeDto.class,
      ContainerType.class,
      filterResolver,
      null,
      props);
  }

}
