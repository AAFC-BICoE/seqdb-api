package ca.gc.aafc.seqdb.api.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import com.google.common.base.Objects;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.util.NumberLetterMappingUtils;
import lombok.NonNull;

@Repository
public class LibraryPrepRepository extends DinaRepository<LibraryPrepDto, LibraryPrep> {

  private final BaseDAO baseDAO;
  private final DinaService<LibraryPrep> libraryPrepService;

  public LibraryPrepRepository(
    @NonNull DinaService<LibraryPrep> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService,
    BaseDAO baseDAO
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(LibraryPrepDto.class),
      LibraryPrepDto.class,
      LibraryPrep.class,
      filterResolver);
      this.baseDAO = baseDAO;
      this.libraryPrepService = dinaService;
  }

  @Transactional
  @Override
  public <S extends LibraryPrepDto> S save(S resource) {
    this.handleOverlap(resource);
    S libraryPrep = super.save(resource);
    this.validateCoordinates(libraryPrep);
    return libraryPrep;
  }

  @Transactional
  @Override
  public <S extends LibraryPrepDto> S create(S resource) {
    this.handleOverlap(resource);
    S libraryPrep = super.create(resource);
    this.validateCoordinates(libraryPrep);
    return libraryPrep;
  }
  
  private void handleOverlap(LibraryPrepDto libraryPrepDto) {
    String row = libraryPrepDto.getWellRow();
    Integer col = libraryPrepDto.getWellColumn();
    
    if (libraryPrepDto.getWellColumn() != null && libraryPrepDto.getWellRow() != null) {
      LibraryPrepBatch prepBatch = baseDAO.findOneByNaturalId(
        libraryPrepDto.getLibraryPrepBatch().getUuid(),
        LibraryPrepBatch.class
      );
      
      List<LibraryPrep> otherPreps = prepBatch.getLibraryPreps()
          .stream()
          .filter(lp -> !lp.getUuid().equals(libraryPrepDto.getUuid()))
          .collect(Collectors.toList());
      
      for (LibraryPrep otherPrep : otherPreps) {
        // If an existing libraryprep has these coordinates,
        // set the existing libraryprep's coordinates to null:
        if (Objects.equal(col, otherPrep.getWellColumn())
            && Objects.equal(row, otherPrep.getWellRow())) {
          otherPrep.setWellColumn(null);
          otherPrep.setWellRow(null);
          this.libraryPrepService.update(otherPrep);
        }
      }
    }
  }

  private void validateCoordinates(LibraryPrepDto libraryPrepDto) {
    String row = libraryPrepDto.getWellRow();
    Integer col = libraryPrepDto.getWellColumn();
    
    // Row and col must be either both set or both null.
    if (col == null && row != null) {
      throw new ValidationException("Well column cannot be null when well row is set.");
    }
    if (row == null && col != null) {
      throw new ValidationException("Well row cannot be null when well column is set.");
    }

    // Validate well coordinates if they are set.
    if (libraryPrepDto.getWellColumn() != null && libraryPrepDto.getWellRow() != null) {
      LibraryPrep libraryPrep = baseDAO.findOneByNaturalId(libraryPrepDto.getUuid(), LibraryPrep.class);
      
      ContainerType cType = libraryPrep.getLibraryPrepBatch().getContainerType();
      
      Integer rows = cType.getNumberOfRows();
      Integer cols = cType.getNumberOfColumns();
      
      if (col > cols) {
        throw new ValidationException(
            String.format("Well column %s exceeds container's number of columns.", col)
        );
      }
      
      if (NumberLetterMappingUtils.getNumber(row) > rows) {
        throw new ValidationException(
            String.format("Row letter %s exceeds container's number of rows.", row)
        );
      }
    }

  }

}
