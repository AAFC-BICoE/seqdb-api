package ca.gc.aafc.seqdb.api.repository.filter;

import java.io.Serializable;
import java.util.Collections;

import javax.inject.Inject;

import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.repository.BaseRepositoryTest;
import ca.gc.aafc.seqdb.factories.PcrPrimerFactory;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;

public class RsqlFilterHandlerIT extends BaseRepositoryTest {

  @Inject
  private ResourceRepositoryV2<PcrPrimerDto, Serializable> primerRepository;
  
  @Test
  public void findAllPrimers_whenRsqlFilterIsSet_filteredPrimersAreReturned() {
    // Persist 5 test primers.
    persist(PcrPrimerFactory.newPcrPrimer().name("primer1").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer2").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer3").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer4").build());
    persist(PcrPrimerFactory.newPcrPrimer().name("primer5").build());
    
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
}
