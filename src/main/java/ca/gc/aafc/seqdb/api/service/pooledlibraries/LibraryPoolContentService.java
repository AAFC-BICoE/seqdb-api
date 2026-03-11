package ca.gc.aafc.seqdb.api.service.pooledlibraries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ValidationException;

import org.springframework.stereotype.Service;
import org.springframework.validation.SmartValidator;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.service.DefaultDinaService;
import ca.gc.aafc.seqdb.api.entities.libraryprep.IndexSet;
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
    this.validatePool(entity);
  }

  @Override
  protected void preUpdate(LibraryPoolContent entity) {
    this.validatePool(entity);
  }

  /**
   * Warning: targetPool includes the newLpcDto
   * This will fail unless we clear the targetPool first
   * @param lpcDto
   */
  private void validatePool(LibraryPoolContent lpcDto) {
    List<LibraryPrepBatch> alreadyPooledBatches = getBatches(lpcDto.getLibraryPool());
    alreadyPooledBatches.addAll(getBatches(lpcDto.getPooledLibraryPool()));

    List<Integer> duplicatedPrepBatches = alreadyPooledBatches.stream().map(LibraryPrepBatch::getId)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // Group by element and count
      .entrySet()
      .stream()
      .filter(entry -> entry.getValue() > 1) // Filter for entries with count > 1
      .map(Map.Entry::getKey)
      .toList(); // Collect the duplicate elements

    List<Integer> duplicatedIndexSet = alreadyPooledBatches.stream().map(
        LibraryPrepBatch::getIndexSet)
      .filter(Objects::nonNull)
      .map(IndexSet::getId)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // Group by element and count
      .entrySet()
      .stream()
      .filter(entry -> entry.getValue() > 1) // Filter for entries with count > 1
      .map(Map.Entry::getKey) // Map to the element (key)
      .toList(); // Collect the duplicate elements

    if (!duplicatedPrepBatches.isEmpty()) {
      LibraryPrepBatch s = getLibraryPrepBatchById(duplicatedPrepBatches.getFirst(), alreadyPooledBatches);
      throw new ValidationException(
        String.format(
          "Duplicate library prep batch usage: Batch '%s' is already pooled.",
          s.getName()
        )
      );
    }

    if (!duplicatedIndexSet.isEmpty()) {
      IndexSet s = getIndexSetById(duplicatedIndexSet.getFirst(), alreadyPooledBatches);
      throw new ValidationException(
        String.format(
          "Duplicate index set usage: '%s'", s.getName()
        )
      );
    }

//    List<LibraryPrepBatch> newPooledBatches = this.getBatches(newLpcDto);
//    for (LibraryPrepBatch newPooledBatch : newPooledBatches) {
//      for (LibraryPrepBatch alreadyPooledBatch : alreadyPooledBatches) {
//        // Check for duplicate LibraryPrepBatch usage:
//        if (newPooledBatch == alreadyPooledBatch) {
//          throw new ValidationException(
//            String.format(
//              "Duplicate library prep batch usage: Batch '%s' is already pooled.",
//              newPooledBatch.getName()
//            )
//          );
//        }
//
//        // Check for duplicate IndexSet usage:
//        if (newPooledBatch.getIndexSet() != null
//            && newPooledBatch.getIndexSet() == alreadyPooledBatch.getIndexSet()) {
//          throw new ValidationException(
//            String.format(
//              "Duplicate index set usage: Batches '%s' and '%s' are both using index set '%s'",
//              newPooledBatch.getName(), alreadyPooledBatch.getName(), newPooledBatch.getIndexSet().getName()
//            )
//          );
//        }
//      }
//    }
  }

  private static LibraryPrepBatch getLibraryPrepBatchById(Integer id, List<LibraryPrepBatch> list) {
    return list.stream().filter(i -> Objects.equals(i.getId(), id)).findFirst().orElse(null);
  }

  private static IndexSet getIndexSetById(Integer id, List<LibraryPrepBatch> list) {
    return list.stream().map(LibraryPrepBatch::getIndexSet)
      .filter(i -> Objects.equals(i.getId(), id)).findFirst().orElse(null);
  }
  
//  private List<LibraryPrepBatch> getBatches(LibraryPoolContent lpc) {
//
//    List<LibraryPrepBatch> batches = new ArrayList<>();
//
//    if (lpc.getPooledLibraryPool() != null) {
//      LibraryPool pooledPool = lpc.getPooledLibraryPool();
//      batches.addAll(getBatches(pooledPool));
//    }
//    if (lpc.getPooledLibraryPrepBatch() != null) {
//      LibraryPrepBatch pooledBatch = lpc.getPooledLibraryPrepBatch();
//      batches.add(pooledBatch);
//    }
//    return batches;
//  }
  
  private List<LibraryPrepBatch> getBatches(LibraryPool pool) {

    if (pool == null) {
      return List.of();
    }

    List<LibraryPrepBatch> batches = new ArrayList<>();
    List<LibraryPoolContent> poolContents = Optional.ofNullable(pool.getContents())
      .orElse(new ArrayList<>());

    for (LibraryPoolContent content : poolContents) {
      if (content.getPooledLibraryPool() != null) {
        batches.addAll(getBatches(content.getPooledLibraryPool()));
      }
      if (content.getPooledLibraryPrepBatch() != null) {
        batches.add(content.getPooledLibraryPrepBatch());
      }
    }
    return batches;
  }
}
