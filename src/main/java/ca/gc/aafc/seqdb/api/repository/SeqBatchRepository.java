package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.SeqBatchDto;
import ca.gc.aafc.seqdb.api.entities.SeqBatch;
import lombok.NonNull;

@Repository
public class SeqBatchRepository extends DinaRepository<SeqBatchDto, SeqBatch> {  
  
  public SeqBatchRepository(
    @NonNull DinaService<SeqBatch> dinaService,
    Optional<DinaAuthorizationService> groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(SeqBatchDto.class),
      SeqBatchDto.class,
      SeqBatch.class,
      null,
      externalResourceProvider,
      props);
  }

}