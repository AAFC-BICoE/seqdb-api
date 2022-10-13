package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LibraryPoolRepository extends DinaRepository<LibraryPoolDto, LibraryPool> {

  public LibraryPoolRepository(
    @NonNull DinaService<LibraryPool> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      // Make an exception and allow creates when the group is null:
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(LibraryPoolDto.class),
      LibraryPoolDto.class,
      LibraryPool.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }

}
