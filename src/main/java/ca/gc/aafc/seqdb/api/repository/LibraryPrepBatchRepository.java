package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.security.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LibraryPrepBatchRepository extends DinaRepository<LibraryPrepBatchDto, LibraryPrepBatch> {

  public LibraryPrepBatchRepository(
    @NonNull DinaService<LibraryPrepBatch> dinaService,
    Optional<DinaAuthorizationService> groupAuthorizationService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      // Make an exception and allow creates when the group is null:
      groupAuthorizationService.map(auth -> new DinaAuthorizationService() {
        public void authorizeCreate(Object entity) { };
        public void authorizeDelete(Object entity) {
          auth.authorizeDelete(entity);
        };
        public void authorizeUpdate(Object entity) {
          auth.authorizeUpdate(entity);
        };
      }),
      Optional.empty(),
      new DinaMapper<>(LibraryPrepBatchDto.class),
      LibraryPrepBatchDto.class,
      LibraryPrepBatch.class,
      null,
      externalResourceProvider,
      props);
  }

}
