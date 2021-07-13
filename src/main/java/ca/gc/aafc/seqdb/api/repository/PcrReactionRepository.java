package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;
import ca.gc.aafc.seqdb.api.entities.PcrReaction;
import lombok.NonNull;

@Repository
public class PcrReactionRepository extends DinaRepository<PcrReactionDto, PcrReaction> {  
  
  public PcrReactionRepository(
    @NonNull DinaService<PcrReaction> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(PcrReactionDto.class),
      PcrReactionDto.class,
      PcrReaction.class,
      null,
      externalResourceProvider,
      props);
  }

}
