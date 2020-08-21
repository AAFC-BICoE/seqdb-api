package ca.gc.aafc.seqdb.api.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.validation.ValidationException;

import ca.gc.aafc.dina.filter.RsqlFilterHandler;
import ca.gc.aafc.dina.filter.SimpleFilterHandler;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.repository.JpaDtoRepository;
import ca.gc.aafc.dina.repository.JpaResourceRepository;
import ca.gc.aafc.dina.repository.meta.JpaMetaInformationProvider;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;

@Named
public class LibraryPoolContentRepository extends JpaResourceRepository<LibraryPoolContentDto> {

  private final BaseDAO baseDao;

  public LibraryPoolContentRepository(
    JpaDtoRepository dtoRepository,
    SimpleFilterHandler simpleFilterHandler,
    RsqlFilterHandler rsqlFilterHandler,
    JpaMetaInformationProvider metaInformationProvider,
    BaseDAO baseDao
  ) {
    super(
      LibraryPoolContentDto.class,
      dtoRepository,
      Arrays.asList(simpleFilterHandler, rsqlFilterHandler),
      metaInformationProvider
    );
    this.baseDao = baseDao;
  }
  
  @Override
  public <S extends LibraryPoolContentDto> S create(S resource) {
    LibraryPoolContentDto newLpcDto = resource;
    this.validateUniqueIndexSets(newLpcDto, newLpcDto.getLibraryPool());
    return super.create(resource);
  }
  
  private void validateUniqueIndexSets(LibraryPoolContentDto newLpcDto, LibraryPoolDto targetPoolDto) {
    LibraryPool targetPool = baseDao.findOneByNaturalId(targetPoolDto.getUuid(), LibraryPool.class);
    
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
    List<LibraryPrepBatch> batchs = new ArrayList<>();
    
    if (lpc.getPooledLibraryPool() != null) {
      LibraryPool pooledPool = baseDao
          .findOneByNaturalId(lpc.getPooledLibraryPool().getUuid(), LibraryPool.class);
      batchs.addAll(getBatches(pooledPool));
    }
    if (lpc.getPooledLibraryPrepBatch() != null) {
      LibraryPrepBatch pooledBatch = baseDao
          .findOneByNaturalId(lpc.getPooledLibraryPrepBatch().getUuid(), LibraryPrepBatch.class);
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
