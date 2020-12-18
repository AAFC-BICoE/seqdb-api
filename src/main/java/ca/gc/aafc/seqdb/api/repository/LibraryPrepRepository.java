package ca.gc.aafc.seqdb.api.repository;

import java.util.Optional;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import lombok.NonNull;

@Repository
public class LibraryPrepRepository extends DinaRepository<LibraryPrepDto, LibraryPrep> {

  public LibraryPrepRepository(
    @NonNull DinaService<LibraryPrep> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService,
    @NonNull BuildProperties props) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(LibraryPrepDto.class),
      LibraryPrepDto.class,
      LibraryPrep.class,
      filterResolver,
      null,
      props);
  }

}
