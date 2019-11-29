package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.stereotype.Component;

import com.google.common.base.Objects;

import ca.gc.aafc.seqdb.NumberLetterMappingUtils;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.security.authorization.ReadableGroupFilterHandlerFactory;
import ca.gc.aafc.seqdb.entities.ContainerType;
import ca.gc.aafc.seqdb.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.entities.libraryprep.LibraryPrepBatch;

@Component
public class LibraryPrepRepository extends JpaResourceRepository<LibraryPrepDto> {

  public LibraryPrepRepository(JpaDtoRepository dtoRepository,
      SimpleFilterHandler simpleFilterHandler, RsqlFilterHandler rsqlFilterHandler,
      ReadableGroupFilterHandlerFactory groupFilterFactory,
      JpaMetaInformationProvider metaInformationProvider) {
    super(LibraryPrepDto.class, dtoRepository,
        Arrays.asList(simpleFilterHandler, rsqlFilterHandler), metaInformationProvider);
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
      EntityManager em = this.dtoRepository.getEntityManager();
      LibraryPrepBatch prepBatch = em.find(
          LibraryPrepBatch.class,
          libraryPrepDto.getLibraryPrepBatch().getLibraryPrepBatchId()
      );
      
      List<LibraryPrep> otherPreps = prepBatch.getLibraryPreps()
          .stream()
          .filter(lp -> !lp.getId().equals(libraryPrepDto.getLibraryPrepId()))
          .collect(Collectors.toList());
      
      for (LibraryPrep otherPrep : otherPreps) {
        // If an existing libraryprep has these coordinates,
        // set the existing libraryprep's coordinates to null:
        if (Objects.equal(col, otherPrep.getWellColumn())
            && Objects.equal(row, otherPrep.getWellRow())) {
          otherPrep.setWellColumn(null);
          otherPrep.setWellRow(null);
          em.flush();
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
      EntityManager em = this.dtoRepository.getEntityManager();
      LibraryPrep libraryPrep = em.find(LibraryPrep.class, libraryPrepDto.getLibraryPrepId());
      
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
