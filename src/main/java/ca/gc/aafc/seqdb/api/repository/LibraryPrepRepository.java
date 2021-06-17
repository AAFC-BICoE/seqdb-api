package ca.gc.aafc.seqdb.api.repository;

import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.repository.external.ExternalResourceProvider;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LibraryPrepRepository extends DinaRepository<LibraryPrepDto, LibraryPrep> {

  public LibraryPrepRepository(
    @NonNull DinaService<LibraryPrep> dinaService,
    Optional<DinaAuthorizationService> authService,
    @NonNull BuildProperties props,
    ExternalResourceProvider externalResourceProvider) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(LibraryPrepDto.class),
      LibraryPrepDto.class,
      LibraryPrep.class,
      null,
      externalResourceProvider,
      props);
  }

}
