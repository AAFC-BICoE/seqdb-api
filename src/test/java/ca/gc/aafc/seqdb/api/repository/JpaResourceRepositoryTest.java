package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Comparators;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto.PrimerType;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.Direction;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.queryspec.SortSpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;

public class JpaResourceRepositoryTest extends BaseRepositoryTest {

  private ResourceRepositoryV2<PcrPrimerDto, Serializable> primerRepository;
  private ResourceRepositoryV2<RegionDto, Serializable> regionRepository;

  /**
   * Get the repository facade from crnk, which will invoke all filters, decorators, etc.
   */
  @Before
  public void initRepositories() {
    this.primerRepository = this.resourceRegistry.getEntry(PcrPrimerDto.class)
        .getResourceRepositoryFacade();
    this.regionRepository = this.resourceRegistry.getEntry(RegionDto.class)
        .getResourceRepositoryFacade();
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
  public void findAll_whenSortingByName_resultsAreSorted() {
    
    Region region1 = new Region();
    region1.setName("Internal Transcribed Spacer");
    region1.setSymbol("ITS");
    entityManager.persist(region1);
    
    Region region2 = new Region();
    region2.setName("Cytochrome c oxidase subunit I");
    region2.setSymbol("COI");
    entityManager.persist(region2);
    
    Region region3 = new Region();
    region3.setName("ACA");
    region3.setSymbol("ACA");
    entityManager.persist(region3);
    
    QuerySpec querySpecAscending = new QuerySpec(RegionDto.class);
    querySpecAscending.setSort(Arrays.asList(
        new SortSpec(Arrays.asList("name"), Direction.ASC)
    ));
    ResourceList<RegionDto> regionsWithAscendingNames = regionRepository
        .findAll(querySpecAscending);
    assertTrue(
        "Names must be sorted alphabetically (ascending)",
        Comparators.isInOrder(
            regionsWithAscendingNames.stream()
                .map(RegionDto::getName)
                .collect(Collectors.toList()),
            String::compareTo
        )
    );
    
    QuerySpec querySpecDescending = new QuerySpec(RegionDto.class);
    querySpecDescending.setSort(Arrays.asList(
        new SortSpec(Arrays.asList("name"), Direction.DESC)
    ));
    ResourceList<RegionDto> regionsWithDescendingNames = regionRepository
        .findAll(querySpecDescending);
    assertTrue(
        "Names must be sorted alphabetically (descending)",
        Comparators.isInOrder(
            regionsWithDescendingNames.stream()
                .map(RegionDto::getName)
                .collect(Collectors.toList()),
            (a, b) -> b.compareTo(a)
        )
    );
  }
  
  @Test
  public void findAll_whenPageLimitIsSet_pageSizeIsLimited() {
    final long pageLimit = 9;
    
    for (int i = 1; i <= 100; i++) {
      Region region = new Region();
      region.setName("test region" + i);
      region.setSymbol("test symbol" + i);
      entityManager.persist(region);
    }
    
    QuerySpec querySpec = new QuerySpec(RegionDto.class);
    querySpec.setLimit(pageLimit);
    ResourceList<RegionDto> limitedRegions = regionRepository.findAll(querySpec);
    assertEquals(pageLimit, limitedRegions.size());
  }
  
  @Test
  public void findAll_whenPageOffsetIsSet_pageStartsAfterOffset() {
    List<Region> newRegions = new ArrayList<>();
    for (int i = 1; i <= 100; i++) {
      Region region = new Region();
      region.setName("test region" + i);
      region.setSymbol("test symbol" + i);
      newRegions.add(region);
      entityManager.persist(region);
    }
    
    final int offset = 15;
    final Integer expectedRegionId = newRegions.get(offset).getId();
    assertNotNull(expectedRegionId);
    
    QuerySpec querySpec = new QuerySpec(RegionDto.class);
    querySpec.setOffset(offset);
    List<RegionDto> regionDtos = regionRepository.findAll(querySpec);
    
    assertEquals(expectedRegionId, regionDtos.get(0).getTagId());
  }
  
  @Test
  public void createPrimer_onSuccess_returnPrimerWithId() {
    PcrPrimerDto newPrimer = new PcrPrimerDto();
    newPrimer.setName(TEST_PRIMER_NAME);
    newPrimer.setLotNumber(TEST_PRIMER_LOT_NUMBER);
    newPrimer.setType(PrimerType.PRIMER);
    newPrimer.setSeq(TEST_PRIMER_SEQ);
    
    PcrPrimerDto createdPrimer = primerRepository.create(newPrimer);
    
    assertNotNull(createdPrimer.getPcrPrimerId());
    assertEquals(TEST_PRIMER_NAME, createdPrimer.getName());
    assertEquals(TEST_PRIMER_LOT_NUMBER, createdPrimer.getLotNumber());
    assertEquals(PrimerType.PRIMER, createdPrimer.getType());
    assertEquals(TEST_PRIMER_SEQ, createdPrimer.getSeq());
    
    PcrPrimer primerEntity = entityManager.find(PcrPrimer.class, createdPrimer.getPcrPrimerId());
    assertNotNull(primerEntity.getPcrPrimerId());
    assertEquals(TEST_PRIMER_NAME, primerEntity.getName());
    assertEquals(TEST_PRIMER_LOT_NUMBER, primerEntity.getLotNumber());
    assertEquals(ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType.PRIMER, primerEntity.getType());
    assertEquals(TEST_PRIMER_SEQ, primerEntity.getSeq());
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
