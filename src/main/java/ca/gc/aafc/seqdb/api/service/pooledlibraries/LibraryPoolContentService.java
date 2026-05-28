package ca.gc.aafc.seqdb.api.service.pooledlibraries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import java.util.function.Function;
import java.util.stream.Collectors;
import jakarta.validation.ValidationException;

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
    this.validatePool(entity);
  }

  @Override
  protected void preUpdate(LibraryPoolContent entity) {
    this.validatePool(entity);
  }

  /**

   * @param lpcDto
   */
  private void validatePool(LibraryPoolContent lpcDto) {
    List<LibraryPrepBatch> alreadyPooledBatches = getAllLibraryPrepBatchesInPool(lpcDto.getLibraryPool());
    alreadyPooledBatches.addAll(getAllLibraryPrepBatchesInPool(lpcDto.getPooledLibraryPool()));

    // Check for duplicate prep batches
    List<Integer> duplicatedPrepBatches = getDuplicatePrepBatches(alreadyPooledBatches);

    if (!duplicatedPrepBatches.isEmpty()) {
      LibraryPrepBatch s = getLibraryPrepBatchById(duplicatedPrepBatches.getFirst(), alreadyPooledBatches);
      throw new ValidationException(
        String.format(
          "Duplicate library prep batch usage: Batch '%s' is already pooled.",
          s.getName()
        )
      );
    }

    // Check for duplicate index sets with batch information
    Map<Integer, List<LibraryPrepBatch>> duplicateIndexSets = getDuplicateIndexSets(alreadyPooledBatches);
    for (List<LibraryPrepBatch> batchesWithDuplicateIndexSet : duplicateIndexSets.values()) {
      LibraryPrepBatch batch1 = batchesWithDuplicateIndexSet.get(0);
      LibraryPrepBatch batch2 = batchesWithDuplicateIndexSet.get(1);

      throw new ValidationException(
        String.format(
          "Duplicate index set usage: Batches '%s' and '%s' are both using index set '%s'",
          batch1.getName(),
          batch2.getName(),
          batch1.getIndexSet().getName()
        )
      );
    }
  }

  /**
   * Extracts library prep batches that are duplicated in the provided list.
   *
   * @param batches the list of library prep batches to analyze
   * @return a list of batch IDs that appear more than once in the input list
   */
  private List<Integer> getDuplicatePrepBatches(List<LibraryPrepBatch> batches) {
    return batches.stream()
      .map(LibraryPrepBatch::getId)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
      .entrySet()
      .stream()
      .filter(entry -> entry.getValue() > 1)
      .map(Map.Entry::getKey)
      .toList();
  }

  /**
   * Extracts library prep batches that share the same index set.
   *
   * @param batches the list of library prep batches to analyze
   * @return a map where keys are index set IDs and values are lists of batches
   *         that share the same index set (only includes index sets used by 2+ batches)
   */
  private Map<Integer, List<LibraryPrepBatch>> getDuplicateIndexSets(List<LibraryPrepBatch> batches) {
    return batches.stream()
      .filter(batch -> batch.getIndexSet() != null)
      .collect(Collectors.groupingBy(
        batch -> batch.getIndexSet().getId(),
        Collectors.toList()
      ))
      .entrySet()
      .stream()
      .filter(entry -> entry.getValue().size() > 1)
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static LibraryPrepBatch getLibraryPrepBatchById(Integer id, List<LibraryPrepBatch> list) {
    return list.stream().filter(i -> Objects.equals(i.getId(), id)).findFirst().orElse(null);
  }

  /**
   * Recursively retrieves all library prep batches contained within a library pool,
   * including batches from nested pools.
   *
   * @param pool the library pool to extract batches from
   * @return a list of all library prep batches in the pool and its nested pools,
   *         or an empty list if the pool is null
   */
  private List<LibraryPrepBatch> getAllLibraryPrepBatchesInPool(LibraryPool pool) {

    if (pool == null) {
      return List.of();
    }

    List<LibraryPrepBatch> batches = new ArrayList<>();
    List<LibraryPoolContent> poolContents = Optional.ofNullable(pool.getContents())
      .orElse(new ArrayList<>());

    for (LibraryPoolContent content : poolContents) {
      if (content.getPooledLibraryPool() != null) {
        batches.addAll(getAllLibraryPrepBatchesInPool(content.getPooledLibraryPool()));
      }
      if (content.getPooledLibraryPrepBatch() != null) {
        batches.add(content.getPooledLibraryPrepBatch());
      }
    }
    return batches;
  }
}
