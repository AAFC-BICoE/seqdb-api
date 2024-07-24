package ca.gc.aafc.seqdb.api.service.pooledlibraries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.ValidationException;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;
import lombok.NonNull;

@Service
public class LibraryPoolContentService extends DefaultDinaService<LibraryPoolContent> {

  public LibraryPoolContentService(
    @NonNull BaseDAO baseDAO,
    @NonNull SmartValidator sv) {
    super(baseDAO, sv);
  }

  @Override
  protected void preCreate(LibraryPoolContent entity) {
    entity.setUuid(UUID.randomUUID());

    this.validateUniqueIndexSets(entity, entity.getLibraryPool());
  }

  private void validateUniqueIndexSets(LibraryPoolContent newLpcDto, LibraryPool targetPool) {
    List<LibraryPrepBatch> newPooledBatches = this.getBatches(newLpcDto);
    List<LibraryPrepBatch> alreadyPooledBatches = getBatches(targetPool);
    
    for (LibraryPrepBatch newPooledBatch : newPooledBatches) {
      for (LibraryPrepBatch alreadyPooledBatch : alreadyPooledBatches) {
        // Check for duplicate LibraryPrepBatch usage:
        if (newPooledBatch == alreadyPooledBatch) {
          throw new ValidationException(
            String.format(
              "Duplicate libary prep batch usage: Batch '%s' is already pooled.",
              newPooledBatch.getName()
            )
          );
        }
        
        // Check for duplicate IndexSet usage:
        if (newPooledBatch.getIndexSet() != null
            && newPooledBatch.getIndexSet() == alreadyPooledBatch.getIndexSet()) {
          throw new ValidationException(
            String.format(
              "Duplicate index set usage: Batches '%s' and '%s' are both using index set '%s'",
              newPooledBatch.getName(), alreadyPooledBatch.getName(), newPooledBatch.getIndexSet().getName()
            )
          );
        }
      }
    }
  }
  
  private List<LibraryPrepBatch> getBatches(LibraryPoolContent lpc) {
    List<LibraryPrepBatch> batchs = new ArrayList<>();
    
    if (lpc.getPooledLibraryPool() != null) {
      LibraryPool pooledPool = lpc.getPooledLibraryPool();
      batchs.addAll(getBatches(pooledPool));
    }
    if (lpc.getPooledLibraryPrepBatch() != null) {
      LibraryPrepBatch pooledBatch = lpc.getPooledLibraryPrepBatch();
      batchs.add(pooledBatch);
    }
    
    return batchs;
  }
  
  private List<LibraryPrepBatch> getBatches(LibraryPool pool) {
    List<LibraryPrepBatch> batchs = new ArrayList<>();
    List<LibraryPoolContent> poolContents = Optional.ofNullable(pool.getContents())
      .orElse(new ArrayList<>());

    for (LibraryPoolContent content : poolContents) {
      if (content.getPooledLibraryPool() != null) {
        batchs.addAll(getBatches(content.getPooledLibraryPool()));
      }
      if (content.getPooledLibraryPrepBatch() != null) {
        batchs.add(content.getPooledLibraryPrepBatch());
      }
    }

    return batchs;
  }
}
