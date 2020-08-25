package ca.gc.aafc.seqdb.api.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.springframework.stereotype.Repository;

import ca.gc.aafc.dina.filter.DinaFilterResolver;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.mapper.DinaMapper;
import ca.gc.aafc.dina.repository.DinaRepository;
import ca.gc.aafc.dina.service.DinaAuthorizationService;
import ca.gc.aafc.dina.service.DinaService;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.entities.libraryprep.LibraryPrepBatch;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPool;
import ca.gc.aafc.seqdb.api.entities.pooledlibraries.LibraryPoolContent;
import lombok.NonNull;

@Repository
public class LibraryPoolContentRepository extends DinaRepository<LibraryPoolContentDto, LibraryPoolContent> {

  private final DinaService<LibraryPoolContent> libraryPoolService;

  public LibraryPoolContentRepository(
    @NonNull DinaService<LibraryPoolContent> dinaService,
    @NonNull DinaFilterResolver filterResolver,
    Optional<DinaAuthorizationService> authService
  ) {
    super(
      dinaService,
      authService,
      Optional.empty(),
      new DinaMapper<>(LibraryPoolContentDto.class),
      LibraryPoolContentDto.class,
      LibraryPoolContent.class,
      filterResolver);
      this.libraryPoolService = dinaService;
  }

  @Override
  public <S extends LibraryPoolContentDto> S create(S resource) {
    LibraryPoolContentDto newLpcDto = resource;
    this.validateUniqueIndexSets(newLpcDto, newLpcDto.getLibraryPool());
    return super.create(resource);
  }
  
  private void validateUniqueIndexSets(LibraryPoolContentDto newLpcDto, LibraryPoolDto targetPoolDto) {
    LibraryPool targetPool = libraryPoolService.findOne(targetPoolDto.getUuid(), LibraryPool.class);
    
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
      LibraryPool pooledPool = libraryPoolService
          .findOne(lpc.getPooledLibraryPool().getUuid(), LibraryPool.class);
      batchs.addAll(getBatches(pooledPool));
    }
    if (lpc.getPooledLibraryPrepBatch() != null) {
      LibraryPrepBatch pooledBatch = libraryPoolService
          .findOne(lpc.getPooledLibraryPrepBatch().getUuid(), LibraryPrepBatch.class);
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
