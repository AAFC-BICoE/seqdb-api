package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.list.ResourceList;

public class JpaRelationshipRepositoryIT extends BaseRepositoryTest {

  @Inject
  private JpaResourceRepository<PcrPrimerDto> primerRepository;
  
  @Inject
  private JpaResourceRepository<PcrBatchDto> pcrBatchRepository;
  
  @Inject
  private JpaResourceRepository<PcrReactionDto> pcrReactionRepository;

  @Inject
  private JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository;
  
  @Inject
  private JpaRelationshipRepository<PcrBatchDto, PcrReactionDto> pcrBatchToReactionRepository;
  
  @Inject
  private JpaRelationshipRepository<PcrReactionDto, PcrBatchDto> pcrReactionToBatchRepository;
  
  @Test
  public void findOneTargetRegionFromSourcePrimer_whenNoFieldsAreSelected_regionReturnedWithAllFields() {
    PcrPrimer primer = persistTestPrimerWithRegion();
    
    QuerySpec querySpec = new QuerySpec(RegionDto.class);
    
    RegionDto region = primerToRegionRepository.findOneTarget(primer.getId(), "region", querySpec);
    
    assertNotNull(region.getTagId());
    assertNotNull(region.getName());
    assertNotNull(region.getDescription());
    assertNotNull(region.getSymbol());
  }
  
  @Test
  public void findOneTargetRegionFromSourcePrimer_whenFieldsAreSelected_regionReturnedWithSelectedFields() {
    PcrPrimer primer = persistTestPrimerWithRegion();
    
    QuerySpec targetQuerySpec = new QuerySpec(RegionDto.class);
    targetQuerySpec.setIncludedFields(includeFieldSpecs("name", "description"));
    
    RegionDto region = primerToRegionRepository.findOneTarget(primer.getId(), "region", targetQuerySpec);
    
    assertNotNull(region.getTagId());
    assertNotNull(region.getName());
    assertNotNull(region.getDescription());
    assertNull(region.getSymbol());
  }
  
  @Test(expected = ResourceNotFoundException.class)
  public void findOneTargetRegionFromSourcePrimer_whenPrimerExistsAndRegionDoesNotExist_throwResourceNotFoundException() {
    PcrPrimer primer = persistTestPrimer();
    
    QuerySpec targetQuerySpec = new QuerySpec(RegionDto.class);
    primerToRegionRepository.findOneTarget(primer.getId(), "region", targetQuerySpec);
  }
  
  @Test
  public void findManyTargetReactionsFromPcrBatch_whenNoParamsAreSet_allReactionsAreReturned() {
    PcrBatch batch = persistTestPcrBatchWith22Reactions("test batch");
    
    // Add a second batch to ensure that the repository does not fetch reactions from a different
    // batch.
    persistTestPcrBatchWith22Reactions("unrelated batch");
    
    QuerySpec querySpec = new QuerySpec(PcrReactionDto.class);
    
    ResourceList<PcrReactionDto> reactionDtos = pcrBatchToReactionRepository
        .findManyTargets(batch.getId(), "reactions", querySpec);
    
    assertEquals(22, reactionDtos.size());
    assertEquals(
        reactionDtos.stream()
            .map(PcrReactionDto::getPcrReactionId)
            .sorted()
            .collect(Collectors.toList()),
        batch.getReactions().stream()
            .map(PcrReaction::getPcrReactionId)
            .sorted()
            .collect(Collectors.toList())
    );
    
  }
  
  @Test
  public void setRelation_whenDtoPrimerRegionIsChanged_primerEntityRelatedRegionIsChanged() {
    PcrPrimer testPrimer = persistTestPrimerWithRegion();
    
    Region newRegion = new Region();
    newRegion.setName("new region");
    newRegion.setDescription(TEST_REGION_DESCRIPTION);
    newRegion.setSymbol(TEST_REGION_SYMBOL);
    entityManager.persist(newRegion);
    
    PcrPrimerDto testPrimerDto = primerRepository.findOne(
        testPrimer.getId(),
        new QuerySpec(PcrPrimerDto.class)
    );
    
    primerToRegionRepository.setRelation(testPrimerDto, newRegion.getId(), "region");
    
    assertEquals(newRegion.getId(), testPrimer.getRegion().getId());
  }
  
  @Test
  public void setRelation_whenPcrReactionIsMovedToDifferentBatch_reactionEntiityRelatedBatchIsChanged() {
    // Create 2 batches. We will move a reaction from batch1 to batch2.
    PcrBatch batch1 = persistTestPcrBatchWith22Reactions("batch1");
    PcrBatch batch2 = persistTestPcrBatchWith22Reactions("batch2");
    
    PcrReaction reactionEntityToMove = batch1.getReactions().get(0);
    
    PcrReactionDto reactionDtoToMove = pcrReactionRepository.findOne(
        reactionEntityToMove.getPcrReactionId(),
        new QuerySpec(PcrReactionDto.class)
    );

    // Move the reaction.
    pcrReactionToBatchRepository.setRelation(reactionDtoToMove, batch2.getPcrBatchId(), "pcrBatch");
    
    assertEquals(batch2, reactionEntityToMove.getPcrBatch());
  }
  
  @Test
  public void setRelationPcrReactionToPcrBatch_whenReactionAlreadyLinkedToBatch_relationDoesNotChange() {
    PcrBatch batch = persistTestPcrBatchWith22Reactions("batch");
    PcrReaction reaction = batch.getReactions().get(0);
    PcrReactionDto reactionDto = pcrReactionRepository.findOne(
        reaction.getPcrReactionId(),
        new QuerySpec(PcrReactionDto.class)
    );
    
    // Do the redundant setRelation.
    pcrReactionToBatchRepository.setRelation(reactionDto, batch.getPcrBatchId(), "pcrBatch");
    
    // Check that the reaction is still linked to the same batch.
    assertEquals(batch, reaction.getPcrBatch());
  }
  
  @Test
  public void setRelations_whenPcrReactionsAreMovedToDifferentPcrBatch_entityRelationsAreSuccessfullyChanged() {
    // Create a test batch with 22 reactions.
    PcrBatch batch1 = persistTestPcrBatchWith22Reactions("batch1");
    
    // Create a batch with no reactions.
    PcrBatch batch2 = new PcrBatch();
    batch2.setName("batch2");
    batch2.setType(PcrBatchType.SANGER);
    batch2.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
    entityManager.persist(batch2);
    
    // Batch 1 should have 22 reactions and batch2 should have no reactions.
    assertEquals(22, batch1.getReactions().size());
    assertEquals(0, batch2.getReactions().size());
    
    // Get the batch2 DTO.
    PcrBatchDto batch2Dto = pcrBatchRepository.findOne(batch2.getId(), new QuerySpec(PcrBatchDto.class));
    
    // Move batch1's reactions to batch2
    pcrBatchToReactionRepository.setRelations(
        batch2Dto,
        batch1.getReactions().stream().map(PcrReaction::getId).collect(Collectors.toList()),
        "reactions"
    );
    
    // The 22 reactions should have been moved from batch1 to batch2.
    assertEquals(22, batch2.getReactions().size());
    for (PcrReaction reaction : batch2.getReactions()) {
      assertEquals(batch2, reaction.getPcrBatch());
    }
  }
  
  @Test
  public void addRelations_whenPcrReactionsAreMovedToDifferentPcrBatch_entityRelationsAreSuccessfullyChanged() {
    // Create a test batch with 22 reactions.
    PcrBatch batch1 = persistTestPcrBatchWith22Reactions("batch1");
    
    // Create a batch with no reactions.
    PcrBatch batch2 = new PcrBatch();
    batch2.setName("batch2");
    batch2.setType(PcrBatchType.SANGER);
    batch2.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
    entityManager.persist(batch2);
    
    // Batch 1 should have 22 reactions and batch2 should have no reactions.
    assertEquals(22, batch1.getReactions().size());
    assertEquals(0, batch2.getReactions().size());
    
    // Get the batch2 DTO.
    PcrBatchDto batch2Dto = pcrBatchRepository.findOne(batch2.getId(), new QuerySpec(PcrBatchDto.class));
    
    // Move 3 reactions from batch1 to batch2.
    List<Serializable> reactionIds = Arrays.asList(
        batch1.getReactions().get(0).getId(),
        batch1.getReactions().get(1).getId(),
        batch1.getReactions().get(2).getId()
    );
    pcrBatchToReactionRepository.addRelations(
        batch2Dto,
        reactionIds,
        "reactions"
    );
    
    // The 3 reactions should have been moved from batch1 to batch2.
    assertEquals(
        reactionIds,
        batch2.getReactions().stream().map(PcrReaction::getId).collect(Collectors.toList())
    );
    for (PcrReaction reaction : batch2.getReactions()) {
      assertEquals(batch2, reaction.getPcrBatch());
    }
  }
  
  @Test
  public void removeRelations_whenDtoPrimerRegionRelationIsRemoved_entityRelationIsRemoved() {
    // Create a test primer with a linked region.
    PcrPrimer testPrimer = persistTestPrimerWithRegion();
    
    // The primer should be linked to a region.
    assertNotNull(testPrimer.getRegion());
    
    // Get the test primer's DTO.
    PcrPrimerDto primerDto = primerRepository.findOne(testPrimer.getId(), new QuerySpec(PcrPrimerDto.class));
    
    // Remove the primer's link to the region.
    primerToRegionRepository.removeRelations(
        primerDto,
        Arrays.asList(testPrimer.getRegion().getId()),
        "region"
    );
    
    // The primer should not be linked to the region anymore.
    assertNull(testPrimer.getRegion());
  }
  
}
