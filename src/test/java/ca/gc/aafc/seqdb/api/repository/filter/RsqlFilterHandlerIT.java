package ca.gc.aafc.seqdb.api.repository.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Sets;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.testsupport.factories.PcrPrimerFactory;
import io.crnk.core.engine.information.resource.ResourceInformation;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.PathSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.queryspec.mapper.QuerySpecUrlMapper;
import io.crnk.core.repository.ResourceRepository;
import io.crnk.core.resource.list.ResourceList;

public class RsqlFilterHandlerIT extends BaseRepositoryTest {

  @Inject
  private ResourceRepository<PcrPrimerDto, Serializable> primerRepository;
  
  @Inject
  private QuerySpecUrlMapper querySpecUrlMapper;
  
  @BeforeEach
  public void initPrimers() {
    // Persist 5 test primers.
    persist(PcrPrimerFactory.newPcrPrimer().name("primer1").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer2").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer3").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer4").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer5").build());
  }
  
  @Test
  public void findAllPrimers_whenRsqlFilterIsSet_filteredPrimersAreReturned() {
    // Filter by name = "primer2" or name = "primer4".
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.setFilters(
        Collections.singletonList(
            new FilterSpec(
                Collections.singletonList("rsql"),
                FilterOperator.EQ,
                "name==primer2 or name==primer4" // RSQL string
            )
        )
    );
    
    // Check that the 2 primers were returned.
    ResourceList<PcrPrimerDto> primers = this.primerRepository.findAll(querySpec);
    assertEquals(2, primers.size());
    assertEquals("primer2", primers.get(0).getName());
    assertEquals("primer4", primers.get(1).getName());
  }
  
  @Test
  public void findAllPrimers_whenRqslFilterIsBlank_allPrimersAreReturned() {
    // Filter by name = "primer2" or name = "primer4".
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.setFilters(
        Collections.singletonList(
            new FilterSpec(
                Collections.singletonList("rsql"),
                FilterOperator.EQ,
                "" // Blank RSQL string
            )
        )
    );
    
    ResourceList<PcrPrimerDto> primers = this.primerRepository.findAll(querySpec);
    assertEquals(5, primers.size());
  }
  
  @Test
  public void findAllPrimers_whenRsqlFilterHasCommas_filteredPrimersAreReturned() {
    // Filter by name = "primer2" or name = "primer4".
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.setFilters(
        Collections.singletonList(
            new FilterSpec(
                Collections.singletonList("rsql"),
                FilterOperator.EQ,
                "name==primer2,name==primer4" // RSQL string
            )
        )
    );
    
    // Check that the 2 primers were returned.
    ResourceList<PcrPrimerDto> primers = this.primerRepository.findAll(querySpec);
    assertEquals(2, primers.size());
    assertEquals("primer2", primers.get(0).getName());
    assertEquals("primer4", primers.get(1).getName());
  }
  
  /**
   * For RSQL strings containing commas to work, the Crnk QuerySpecUrlMapper needs to be
   * configured to not convert those strings to HashSets. This test ensures that the
   * QuerySpecUrlMapper is configured correctly.
   */
  @Test
  public void deserializeFilterParam_whenParamHasComma_deserializeFilterAsString() {
    ResourceInformation primerInfo = resourceRegistry.findEntry(PcrPrimerDto.class).getResourceInformation();
    
    Map<String, Set<String>> paramMap = new HashMap<>();
    paramMap.put("filter[rsql]", Sets.newHashSet("name==asd,asd,asd,asd"));
    
    QuerySpec querySpec = querySpecUrlMapper.deserialize(primerInfo, paramMap);
    
    assertEquals(
        "name==asd,asd,asd,asd",
        querySpec.findFilter(PathSpec.of("rsql")).get().getValue()
    );
  }
}
