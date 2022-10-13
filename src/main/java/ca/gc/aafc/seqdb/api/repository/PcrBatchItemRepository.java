package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.pcr.PcrBatchItemDto;
import ca.gc.aafc.seqdb.api.entities.pcr.PcrBatchItem;
import lombok.NonNull;

@Repository
public class PcrBatchItemRepository extends DinaRepository<PcrBatchItemDto, PcrBatchItem> {  
  
  public PcrBatchItemRepository(
    @NonNull DinaService<PcrBatchItem> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(PcrBatchItemDto.class),
      PcrBatchItemDto.class,
      PcrBatchItem.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }

}

