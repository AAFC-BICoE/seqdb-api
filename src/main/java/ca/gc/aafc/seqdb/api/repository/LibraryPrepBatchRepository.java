package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import lombok.NonNull;

@Repository
public class LibraryPrepBatchRepository extends DinaRepository<LibraryPrepBatchDto, LibraryPrepBatch> {

  public LibraryPrepBatchRepository(
    @NonNull DinaService<LibraryPrepBatch> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      // Make an exception and allow creates when the group is null:
      authService.map(auth -> new DinaAuthorizationService() {
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
      filterResolver,
      null);
  }

}
