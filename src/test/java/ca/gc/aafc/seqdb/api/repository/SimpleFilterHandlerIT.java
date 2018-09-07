package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class SimpleFilterHandlerIT extends BaseRepositoryTest {

  @Inject
  private ResourceRepositoryV2<PcrPrimerDto, Serializable> primerRepository;
  
  @Test
  public void searchPrimers_whenNameFilterIsSet_filteredPrimersAreReturned() {
    
    final String expectedPrimerName = "primer2";
    
    PcrPrimer primer1 = new PcrPrimer();
    primer1.setName("primer1");
    
    PcrPrimer primer2 = new PcrPrimer();
    primer2.setName(expectedPrimerName);
    
    PcrPrimer primer3 = new PcrPrimer();
    primer3.setName("primer3");
    
    PcrPrimer primer20 = new PcrPrimer();
    primer20.setName("primer20");
    
    for (PcrPrimer newPrimer : Arrays.asList(primer1, primer2, primer3, primer20)) {
      newPrimer.setLotNumber(TEST_PRIMER_LOT_NUMBER);
      newPrimer.setType(TEST_PRIMER_TYPE);
      newPrimer.setSeq(TEST_PRIMER_SEQ);
      entityManager.persist(newPrimer);
    }
    
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.addFilter(new FilterSpec(Arrays.asList("name"), FilterOperator.EQ, expectedPrimerName));
    List<PcrPrimerDto> primerDtos = this.primerRepository.findAll(querySpec);
    
    assertEquals(
        Arrays.asList(expectedPrimerName),
        primerDtos.stream().map(PcrPrimerDto::getName).collect(Collectors.toList())
    );
    
  }

}
