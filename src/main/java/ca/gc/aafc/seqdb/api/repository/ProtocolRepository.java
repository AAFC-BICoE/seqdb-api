package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.ProtocolDto;
import ca.gc.aafc.seqdb.api.entities.Protocol;
import lombok.NonNull;

@Repository
public class ProtocolRepository extends DinaRepository<ProtocolDto, Protocol> {

  public ProtocolRepository(
    @NonNull DinaService<Protocol> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService,
    @NonNull BuildProperties props) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(ProtocolDto.class),
      ProtocolDto.class,
      Protocol.class,
      filterResolver,
      null,
      props);
  }

}
