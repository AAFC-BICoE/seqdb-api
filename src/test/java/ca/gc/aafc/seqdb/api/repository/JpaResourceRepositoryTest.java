package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.IncludeRelationSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class JpaResourceRepositoryTest extends BaseRepositoryTest {

  private ResourceRepositoryV2<PcrPrimerDto, Serializable> primerRepository;

  /**
   * Get the repository facade from crnk, which will invoke all filters, decorators, etc.
   */
  @Before
  public void initRepository() {
    this.primerRepository = this.resourceRegistry.getEntry(PcrPrimerDto.class).getResourceRepositoryFacade();
  }

  @Test
  public void findOnePrimer_whenNoFieldsAreSelected_primerReturnedWithAllFields() {
    PcrPrimer primer = persistTestPrimer();

    PcrPrimerDto primerDto = primerRepository.findOne(
        primer.getId(),
        new QuerySpec(PcrPrimerDto.class)
    );

    // Returned primer DTO must have correct values: all fields are present because no selected
    // fields were specified in the QuerySpec
    assertNotNull(primerDto);
    assertEquals(primer.getId(), primerDto.getPcrPrimerId());
    assertEquals(TEST_PRIMER_NAME, primerDto.getName());
    assertEquals(TEST_PRIMER_LOT_NUMBER, primerDto.getLotNumber());
    assertEquals(TEST_PRIMER_TYPE.getValue(), primerDto.getType().getValue());
    assertEquals(TEST_PRIMER_SEQ, primerDto.getSeq());

  }

  @Test
  public void findOnePrimer_whenFieldsAreSelected_primerReturnedWithSelectedFieldsOnly() {
    PcrPrimer primer = persistTestPrimer();

    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.setIncludedFields(includeFieldSpecs("name", "lotNumber"));

    PcrPrimerDto primerDto = primerRepository.findOne(primer.getId(), querySpec);

    // Returned primer DTO must have correct values: selected fields are present, non-selected
    // fields are null.
    assertNotNull(primerDto);
    assertEquals(primer.getId(), primerDto.getPcrPrimerId());
    assertEquals(TEST_PRIMER_NAME, primerDto.getName());
    assertEquals(TEST_PRIMER_LOT_NUMBER, primerDto.getLotNumber());
    assertNull(primerDto.getType());
    assertNull(primerDto.getSeq());
  }
  
  @Test
  public void findOnePrimer_whenRegionIsIncludedAndFieldsAreSelected_primerWithRegionReturnedWithSelectedFieldsOnly() {
    PcrPrimer primer = persistTestPrimerWithRegion();
    
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.setIncludedFields(includeFieldSpecs("name", "lotNumber"));
    
    QuerySpec nestedRegionSpec = new QuerySpec(RegionDto.class);
    nestedRegionSpec.setIncludedFields(includeFieldSpecs("name", "description"));
    
    querySpec.setIncludedRelations(includeRelationSpecs("region"));
    querySpec.setNestedSpecs(Arrays.asList(nestedRegionSpec));
    
    PcrPrimerDto primerDto = primerRepository.findOne(primer.getId(), querySpec);
    
    assertNotNull(primerDto.getName());
    assertNotNull(primerDto.getLotNumber());
    
    assertNull(primerDto.getType());
    assertNull(primerDto.getSeq());
    
    assertNotNull(primerDto.getRegion());
    assertNotNull(primerDto.getRegion().getTagId());
    assertNotNull(primerDto.getRegion().getName());
    assertNotNull(primerDto.getRegion().getDescription());
    
    assertNull(primerDto.getRegion().getSymbol());
  }
  
  @Test
  public void findOnePrimer_whenRegionIsIncludedAndNoFieldsAreSelected_primerAndRegionReturnedWithAllFields() {
    PcrPrimer primer = persistTestPrimerWithRegion();
    
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    
    querySpec.setIncludedRelations(includeRelationSpecs("region"));
    
    PcrPrimerDto primerDto = primerRepository.findOne(primer.getId(), querySpec);
    
    assertNotNull(primerDto.getName());
    assertNotNull(primerDto.getLotNumber());
    assertNotNull(primerDto.getType());
    assertNotNull(primerDto.getSeq());
    
    assertNotNull(primerDto.getRegion());
    assertNotNull(primerDto.getRegion().getTagId());
    assertNotNull(primerDto.getRegion().getName());
    assertNotNull(primerDto.getRegion().getDescription());
    assertNotNull(primerDto.getRegion().getSymbol());
  }
  
  @Test
  public void findOnePrimer_whenRegionIsIncludedButDoesNotExist_primerReturnedWithNullRegion() {
    PcrPrimer primer = persistTestPrimer();
    
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.setIncludedRelations(includeRelationSpecs("region"));
    
    PcrPrimerDto primerDto = primerRepository.findOne(primer.getId(), querySpec);
    
    assertNotNull(primerDto.getPcrPrimerId());
    assertNotNull(primerDto.getName());
    assertNull(primerDto.getRegion());
  }
  
  @Test(expected = ResourceNotFoundException.class)
  public void findOnePrimer_onPrimerNotFound_throwsResourceNotFoundException() {
    primerRepository.findOne(1, new QuerySpec(PcrPrimerDto.class));
  }

  @Test
  public void deletePrimer_onPrimerLookup_primerNotFound() {
    PcrPrimer primer = persistTestPrimer();
    primerRepository.delete(primer.getId());
    assertNull(entityManager.find(PcrPrimer.class, primer.getId()));
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deletePrimer_onPrimerNotFound_throwResourceNotFoundException() {
    primerRepository.delete(1);
  }

}
