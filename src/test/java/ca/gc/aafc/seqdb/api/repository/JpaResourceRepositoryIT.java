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
import ca.gc.aafc.seqdb.factories.PcrPrimerFactory;
import ca.gc.aafc.seqdb.factories.RegionFactory;
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
  
  //Assertion values, entity values are defined by the factory unless explicitly set.
  private static final String TEST_PRIMER_NAME = "test primer";
  private static final Integer TEST_PRIMER_LOT_NUMBER = 1;
  private static final PrimerType TEST_PRIMER_TYPE = PrimerType.PRIMER;
  private static final String TEST_PRIMER_SEQ = "CTTGGTCATTTAGAGGAAGTAA";
  
  private static final String TEST_REGION_DESCRIPTION = "test description";
  
  // using factory methods from dbi to create a primer and region and persist them in the repository
  // together
  private PcrPrimer createPersistedPcrPrimerWithRegion() {

    PcrPrimer primer = PcrPrimerFactory.newPcrPrimer()
        .name(TEST_PRIMER_NAME)
        .type(TEST_PRIMER_TYPE)
        .lotNumber(TEST_PRIMER_LOT_NUMBER)
        .seq(TEST_PRIMER_SEQ)
        .build();
    Region region = RegionFactory.newRegion().description(TEST_REGION_DESCRIPTION).build();
    persist(primer);
    persist(region);
    primer.setRegion(region);

    return primer;
  }
  

  @Test
  public void findOnePrimer_whenNoFieldsAreSelected_primerReturnedWithAllFields() {
    
    PcrPrimer primer = createPersistedPcrPrimerWithRegion();

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
    PcrPrimer primer = PcrPrimerFactory.newPcrPrimer()
        .name(TEST_PRIMER_NAME)
        .lotNumber(TEST_PRIMER_LOT_NUMBER)
        .seq(TEST_PRIMER_SEQ)
        .build();
    persist(primer);

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
    
    PcrPrimer primer = createPersistedPcrPrimerWithRegion();
    
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
    
    PcrPrimer primer = createPersistedPcrPrimerWithRegion();
    
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
    
    PcrPrimer primer = PcrPrimerFactory.newPcrPrimer().build();
    persist(primer);
    
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
    
    Region region1 = 
        RegionFactory.newRegion()
        .name("Internal Transcribed Spacer")
        .symbol("ITS")
        .build();    
    persist(region1);
    
    Region region2 = 
        RegionFactory.newRegion()
        .name("Cytochrome c oxidase subunit I")
        .symbol("COI")
        .build();
    persist(region2);
    
    Region region3 = 
        RegionFactory.newRegion()
        .name("ACA")
        .symbol("ACA")
        .build();
    persist(region3);
    
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
      Region region = 
          RegionFactory.newRegion()
          .name("test region" + i)
          .symbol("test symbol" + i)
          .build();
      persist(region);
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
      Region region = 
          RegionFactory.newRegion()
          .name("test region" + i)
          .symbol("test symbol" + i)
          .build();
      newRegions.add(region);
      persist(region);
    }
    
    final int offset = 15;
    final Integer expectedRegionId = newRegions.get(offset).getId();
    assertNotNull(expectedRegionId);

    
    QuerySpec querySpec = new QuerySpec(RegionDto.class);
    querySpec.setOffset(offset + 1); // + 1 since the root node is inserted by liquibase
    List<RegionDto> regionDtos = regionRepository.findAll(querySpec);
    assertEquals(expectedRegionId, regionDtos.get(0).getTagId());
  }
  
  @Test
  public void findAll_whenIdsArgumentIsSet_resultsAreFilteredById() {
    List<Region> newRegions = new ArrayList<>();
    
    for (int i = 1; i <= 10; i++) {
      Region region = 
          RegionFactory.newRegion()
          .name("test region" + i)
          .symbol("test symbol" + i)
          .build();
      newRegions.add(region);
      persist(region);
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
    
    // Check that the reactions were moved to batch2.
    reactions.forEach(reaction -> assertEquals(batch2Entity, reaction.getPcrBatch()));
  }
  
  @Test
  public void savePrimer_onSuccess_primerEntityIsModified() {
    // Create the test primer.
    PcrPrimer testPrimer = PcrPrimerFactory.newPcrPrimer().build();
    persist(testPrimer);
    
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
    PcrPrimer testPrimer = PcrPrimerFactory.newPcrPrimer().build();
    persist(testPrimer);
    
    Region testRegion = RegionFactory.newRegion().build();
    persist(testRegion);
    
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
    PcrPrimer testPrimer = createPersistedPcrPrimerWithRegion();
    
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
    PcrPrimer primer = PcrPrimerFactory.newPcrPrimer().build();
    persist(primer);
    primerRepository.delete(primer.getId());
    assertNull(entityManager.find(PcrPrimer.class, primer.getId()));
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deletePrimer_onPrimerNotFound_throwResourceNotFoundException() {
    primerRepository.delete(1);
  }

}
