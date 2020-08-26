package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import com.google.common.base.Objects;

import org.springframework.stereotype.Service;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.entities.ContainerType;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.util.NumberLetterMappingUtils;
import lombok.NonNull;

@Service
public class LibraryPrepService extends DinaService<LibraryPrep> {

  private BaseDAO baseDAO;

  public LibraryPrepService(@NonNull BaseDAO baseDAO) {
    super(baseDAO);
    this.baseDAO = baseDAO;
  }

  @Override
  protected void preCreate(LibraryPrep entity) {
    entity.setUuid(UUID.randomUUID());
    this.handleOverlap(entity);
  }

  @Override
  protected void preDelete(LibraryPrep entity) {

  }

  @Override
  protected void preUpdate(LibraryPrep entity) {
    this.handleOverlap(entity);
  }

  // Override create to add post-create handling:
  @Override
  public LibraryPrep create(LibraryPrep entity) {
    LibraryPrep result = super.create(entity);
    this.validateCoordinates(result);
    return result;
  }
  
  // Override update to add post-update handling:
  @Override
  public LibraryPrep update(LibraryPrep entity) {
    LibraryPrep result = super.update(entity);
    this.validateCoordinates(result);
    return result;
  }

  private void handleOverlap(LibraryPrep libraryPrep) {
    String row = libraryPrep.getWellRow();
    Integer col = libraryPrep.getWellColumn();
    
    if (libraryPrep.getWellColumn() != null && libraryPrep.getWellRow() != null) {
      LibraryPrepBatch prepBatch = libraryPrep.getLibraryPrepBatch();

      // Flush and refresh the batch to make sure the list of LibraryPreps is up to date:
      baseDAO.createWithEntityManager(em -> {
        em.flush();
        em.refresh(prepBatch);
        return null;
      });
      
      List<LibraryPrep> otherPreps = prepBatch.getLibraryPreps()
          .stream()
          .filter(lp -> !lp.getUuid().equals(libraryPrep.getUuid()))
          .collect(Collectors.toList());
      
      for (LibraryPrep otherPrep : otherPreps) {
        // If an existing libraryprep has these coordinates,
        // set the existing libraryprep's coordinates to null:
        if (Objects.equal(col, otherPrep.getWellColumn())
            && Objects.equal(row, otherPrep.getWellRow())) {
          otherPrep.setWellColumn(null);
          otherPrep.setWellRow(null);
          this.update(otherPrep);
        }
      }
    }
  }

  private void validateCoordinates(LibraryPrep libraryPrep) {
    String row = libraryPrep.getWellRow();
    Integer col = libraryPrep.getWellColumn();
    
    // Row and col must be either both set or both null.
    if (col == null && row != null) {
      throw new ValidationException("Well column cannot be null when well row is set.");
    }
    if (row == null && col != null) {
      throw new ValidationException("Well row cannot be null when well column is set.");
    }

    // Validate well coordinates if they are set.
    if (libraryPrep.getWellColumn() != null && libraryPrep.getWellRow() != null) {
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
