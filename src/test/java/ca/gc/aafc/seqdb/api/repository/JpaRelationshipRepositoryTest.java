package ca.gc.aafc.seqdb.api.repository;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class JpaRelationshipRepositoryTest extends BaseRepositoryTest {

  @Inject
  private JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository;
  
  /**
   * I can't find the equivalent to ResourceRepositoryFacade for relatonship repositories, so I will
   * just set its ResourceRegistry here.
   */
  @Before
  public void initRepository() {
    this.primerToRegionRepository.setResourceRegistry(this.resourceRegistry);
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
  
}
