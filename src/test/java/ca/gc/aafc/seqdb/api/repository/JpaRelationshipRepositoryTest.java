package ca.gc.aafc.seqdb.api.repository;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.list.ResourceList;

public class JpaRelationshipRepositoryTest extends BaseRepositoryTest {

  @Inject
  private JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository;
  
  @Inject
  private JpaRelationshipRepository<PcrBatchDto, PcrReactionDto> pcrBatchToReactionRepository;
  
  /**
   * I can't find the equivalent to ResourceRepositoryFacade for relationship repositories, so I will
   * just set its ResourceRegistry here.
   */
  @Before
  public void initRepository() {
    this.primerToRegionRepository.setResourceRegistry(this.resourceRegistry);
    this.pcrBatchToReactionRepository.setResourceRegistry(this.resourceRegistry);
  }
  
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
  
}
