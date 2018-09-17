package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Test;

import com.google.common.collect.Comparators;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchPlateSize;
import ca.gc.aafc.seqdb.entities.PcrBatch.PcrBatchType;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Region;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.Direction;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.queryspec.SortSpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;

public class JpaResourceRepositoryIT extends BaseRepositoryTest {

  @Inject
  private ResourceRepositoryV2<PcrPrimerDto, Serializable> primerRepository;
  
  @Inject
  private ResourceRepositoryV2<RegionDto, Serializable> regionRepository;
  
  @Inject
  private ResourceRepositoryV2<PcrBatchDto, Serializable> pcrBatchRepository;
  
  @Test
  public void findOnePrimer_whenNoFieldsAreSelected_primerReturnedWithAllFields() {
    PcrPrimer primer = persistTestPrimerWithRegion();

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
    
    // The region ID should be returned, but not the rest of the region's attributes.
    assertNotNull(primerDto.getRegion().getTagId());
    assertNull(primerDto.getRegion().getName());
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
  public void findAll_whenNoSortSpecified_resultsAreUniqueAndSortedByAscendingId() {
    for (int i = 1; i <= 10; i++) {
      this.persistTestPcrBatchWith22Reactions("test batch " + i);
    }
    
    QuerySpec querySpec = new QuerySpec(PcrBatchDto.class);
    querySpec.setLimit(Long.valueOf(10));
    ResourceList<PcrBatchDto> batchDtos = pcrBatchRepository.findAll(querySpec);
    
    // Check that the IDs are in ascending sequence
    Integer idIterator = batchDtos.get(0).getPcrBatchId();
    for (PcrBatchDto batchDto : batchDtos) {
      assertEquals(idIterator++, batchDto.getPcrBatchId());
    }
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
  public void findAll_whenIdsArgumentIsSet_resultsAreFilteredById() {
    List<Region> newRegions = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      Region region = new Region();
      region.setName("test region" + i);
      region.setSymbol("test symbol" + i);
      newRegions.add(region);
      entityManager.persist(region);
    }
    
    Iterable<Serializable> expectedIds = Arrays.asList(
        newRegions.get(2).getId(),
        newRegions.get(4).getId(),
        newRegions.get(6).getId()
    );
    
    QuerySpec querySpec = new QuerySpec(RegionDto.class);
    List<RegionDto> regionDtos = regionRepository.findAll(expectedIds, querySpec);
    
    assertEquals(
        expectedIds,
        regionDtos.stream().map(RegionDto::getTagId).collect(Collectors.toList())
    );
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
  public void createPcrBatch_whenAllReactionsOfADifferentBatchAreLinked_allReactionEntitiesAreReLinked() {
    PcrBatch batch1Entity = persistTestPcrBatchWith22Reactions("batch1");
    
    List<PcrReaction> reactions = batch1Entity.getReactions();
    
    // Set up a PcrBatchDto that will take all of the reactions from batch1.
    PcrBatchDto batch2Dto = new PcrBatchDto();
    batch2Dto.setName("batch2");
    batch2Dto.setPlateSize(PcrBatchPlateSize.PLATE_NUMBER_96);
    batch2Dto.setType(PcrBatchType.SANGER);
    batch2Dto.setReactions(
        // Set the reactions as PcrReactionDtos holding only the ID attribute required for linking.
        reactions.stream()
            .map(PcrReaction::getPcrReactionId)
            .map(reactionId -> {
              PcrReactionDto reactionDto = new PcrReactionDto();
              reactionDto.setPcrReactionId(reactionId);
              return reactionDto;
            })
            .collect(Collectors.toList())
    );
    
    PcrBatchDto savedBatch2Dto = pcrBatchRepository.create(batch2Dto);
    
    PcrBatch batch2Entity = entityManager.find(PcrBatch.class, savedBatch2Dto.getPcrBatchId());
    
    // Check that the reacitons were moved to batch2.
    reactions.forEach(reaction -> assertEquals(batch2Entity, reaction.getPcrBatch()));
  }
  
  @Test
  public void savePrimer_onSuccess_primerEntityIsModified() {
    // Create the test primer.
    PcrPrimer testPrimer = persistTestPrimer();
    
    // Get the test primer's DTO.
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    PcrPrimerDto testPrimerDto = primerRepository.findOne(testPrimer.getId(), querySpec);
    
    // Change the DTO's seq value.
    testPrimerDto.setSeq("edited seq");
    
    // Save the DTO using the repository.
    primerRepository.save(testPrimerDto);
    
    // Check that the primer entity has the new seq value.
    assertEquals("edited seq", testPrimer.getSeq());
  }
  
  @Test
  public void savePrimerWithNewRegion_onSuccess_primerEntityIsModified() {
    // Create the test primer.
    PcrPrimer testPrimer = persistTestPrimer();
    
    Region testRegion = new Region();
    testRegion.setName(TEST_REGION_NAME);
    testRegion.setDescription(TEST_REGION_DESCRIPTION);
    testRegion.setSymbol(TEST_REGION_SYMBOL);
    entityManager.persist(testRegion);
    
    // Get the test primer's DTO.
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    PcrPrimerDto testPrimerDto = primerRepository.findOne(testPrimer.getId(), querySpec);
    
    // Change the test primer's region to the new region.
    RegionDto newRegionDto = new RegionDto();
    newRegionDto.setTagId(testRegion.getId());
    testPrimerDto.setRegion(newRegionDto);
    
    // Save the DTO using the repository.
    PcrPrimerDto updatedPrimerDto = primerRepository.save(testPrimerDto);
    
    // Check that the updated primer has the new region id.
    assertNotNull(testPrimer.getRegion().getTagId());
    assertEquals(testRegion.getTagId(), updatedPrimerDto.getRegion().getTagId());
    assertEquals(testRegion.getTagId(), testPrimer.getRegion().getTagId());
  }
  
  @Test
  public void saveExistingPrimerAndRemoveLinkedRegion_onSuccess_primerEntityIsModified() {
    PcrPrimer testPrimer = persistTestPrimerWithRegion();
    assertNotNull(testPrimer.getRegion().getTagId());
    
    // Get the test primer's DTO.
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    PcrPrimerDto testPrimerDto = primerRepository.findOne(testPrimer.getId(), querySpec);
    
    // The primer's region id should not be null.
    assertNotNull(testPrimerDto.getRegion().getTagId());
    
    testPrimerDto.setRegion(null);

    // Save the DTO using the repository.
    PcrPrimerDto updatedPrimerDto = primerRepository.save(testPrimerDto);
    
    // Check that the region is null in the dto and the entity.
    assertNull(updatedPrimerDto.getRegion());
    assertNull(testPrimer.getRegion());
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
