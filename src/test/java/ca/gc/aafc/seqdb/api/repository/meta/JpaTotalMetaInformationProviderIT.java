package ca.gc.aafc.seqdb.api.repository.meta;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.api.repository.JpaResourceRepository;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.resource.list.ResourceList;
import io.crnk.core.resource.meta.DefaultPagedMetaInformation;

public class JpaTotalMetaInformationProviderIT extends BaseRepositoryTest {

  @Inject
  private JpaResourceRepository<RegionDto> regionRepository;
  
  /**
   * Persist example data for these tests.
   */
  @Before
  public void persistTestRegions() {
    for (int i = 1; i <= 10; i++) {
      Region region = new Region();
      region.setName("test region " + i);
      region.setDescription("desc");
      region.setSymbol("symbol");
      entityManager.persist(region);
    }
  }
  
  @Test
  public void findAll_noAdditionalOptions_fullTotalIsIncluded() {
    ResourceList<RegionDto> regions = regionRepository.findAll(new QuerySpec(RegionDto.class));
    DefaultPagedMetaInformation meta = (DefaultPagedMetaInformation) regions.getMeta();
    assertEquals(10, meta.getTotalResourceCount().longValue());
  }
  
  @Test
  public void findAll_whenFilterIsAdded_reducedTotalIsIncluded() {
    QuerySpec querySpec = new QuerySpec(RegionDto.class);
    querySpec.addFilter(new FilterSpec(Arrays.asList("name"), FilterOperator.EQ, "test region 5"));
    
    ResourceList<RegionDto> regions = regionRepository.findAll(querySpec);
    DefaultPagedMetaInformation meta = (DefaultPagedMetaInformation) regions.getMeta();
    assertEquals(1, regions.size());
    assertEquals(1, meta.getTotalResourceCount().longValue());
  }
  
  @Test
  public void findAll_whenPageLimitIsSpecified_fullTotalIsIncluded() {
    QuerySpec querySpec = new QuerySpec(RegionDto.class);
    querySpec.setLimit(1L);
    
    ResourceList<RegionDto> regions = regionRepository.findAll(querySpec);
    DefaultPagedMetaInformation meta = (DefaultPagedMetaInformation) regions.getMeta();
    assertEquals(1, regions.size());
    assertEquals(10, meta.getTotalResourceCount().longValue());
  }
  
}
