package ca.gc.aafc.seqdb.api.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.ValidationException;

import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.repository.filter.RsqlFilterHandler;
import ca.gc.aafc.seqdb.api.repository.filter.SimpleFilterHandler;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaDtoRepository;
import ca.gc.aafc.seqdb.api.repository.jpa.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.entities.pooledlibraries.LibraryPoolContent;

@Named
public class LibraryPoolContentRepository extends JpaResourceRepository<LibraryPoolContentDto> {

  public LibraryPoolContentRepository(JpaDtoRepository dtoRepository,
      SimpleFilterHandler simpleFilterHandler, RsqlFilterHandler rsqlFilterHandler,
      JpaMetaInformationProvider metaInformationProvider) {
    super(LibraryPoolContentDto.class, dtoRepository,
        Arrays.asList(simpleFilterHandler, rsqlFilterHandler), metaInformationProvider);
  }
  
  @Override
  public <S extends LibraryPoolContentDto> S create(S resource) {
    LibraryPoolContentDto newLpcDto = resource;
    this.validateUniqueIndexSets(newLpcDto, newLpcDto.getLibraryPool());
    return super.create(resource);
  }
  
  private void validateUniqueIndexSets(LibraryPoolContentDto newLpcDto, LibraryPoolDto targetPoolDto) {
    EntityManager em = this.dtoRepository.getEntityManager();
    LibraryPool targetPool = em.find(LibraryPool.class, targetPoolDto.getLibraryPoolId());
    
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
  
  private List<LibraryPrepBatch> getBatches(LibraryPoolContentDto lpc) {
    EntityManager em = this.dtoRepository.getEntityManager();
    List<LibraryPrepBatch> batchs = new ArrayList<>();
    
    if (lpc.getPooledLibraryPool() != null) {
      LibraryPool pooledPool = em
          .find(LibraryPool.class, lpc.getPooledLibraryPool().getLibraryPoolId());
      batchs.addAll(getBatches(pooledPool));
    }
    if (lpc.getPooledLibraryPrepBatch() != null) {
      LibraryPrepBatch pooledBatch = em
          .find(LibraryPrepBatch.class, lpc.getPooledLibraryPrepBatch().getLibraryPrepBatchId());
      batchs.add(pooledBatch);
    }
    
    return batchs;
  }
  
  private List<LibraryPrepBatch> getBatches(LibraryPool pool) {
    List<LibraryPrepBatch> batchs = new ArrayList<>();

    for (LibraryPoolContent content : pool.getContents()) {
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
