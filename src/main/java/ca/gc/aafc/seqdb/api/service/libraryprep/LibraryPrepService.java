package ca.gc.aafc.seqdb.api.service.libraryprep;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.base.Objects;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrep;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.validation.GenericContainerLocationValidator;
import lombok.NonNull;

@Service
public class LibraryPrepService extends DefaultDinaService<LibraryPrep> {

  private BaseDAO baseDAO;
  private final GenericContainerLocationValidator genericContainerLocationValidator;

  public LibraryPrepService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv,
    @NonNull GenericContainerLocationValidator genericContainerLocationValidator) {
    super(baseDAO, sv);
    this.baseDAO = baseDAO;
    this.genericContainerLocationValidator = genericContainerLocationValidator;
  }

  @Override
  protected void preCreate(LibraryPrep entity) {
    entity.setUuid(UUID.randomUUID());
    this.handleOverlap(entity);
  }

  @Override
  protected void preUpdate(LibraryPrep entity) {
    this.handleOverlap(entity);
  }

  // Override create to add post-create handling:
  @Override
  public LibraryPrep create(LibraryPrep entity) {
    LibraryPrep result = super.create(entity);
    return result;
  }
  
  // Override update to add post-update handling:
  @Override
  public LibraryPrep update(LibraryPrep entity) {
    LibraryPrep result = super.update(entity);
    return result;
  }

  @Override
  public void validateBusinessRules(LibraryPrep entity) {
    applyBusinessRule(entity, genericContainerLocationValidator);
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
  
}
