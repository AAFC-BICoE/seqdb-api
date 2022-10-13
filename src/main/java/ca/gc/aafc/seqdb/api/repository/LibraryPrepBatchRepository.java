package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LibraryPrepBatchRepository extends DinaRepository<LibraryPrepBatchDto, LibraryPrepBatch> {

  public LibraryPrepBatchRepository(
    @NonNull DinaService<LibraryPrepBatch> dinaService,
    DinaAuthorizationService groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider,
    ObjectMapper objMapper) {
    super(
      dinaService,
      // Make an exception and allow creates when the group is null:
      groupAuthorizationService,
      Optional.empty(),
      new DinaMapper<>(LibraryPrepBatchDto.class),
      LibraryPrepBatchDto.class,
      LibraryPrepBatch.class,
      null,
      externalResourceProvider,
      props, objMapper);
  }

}
