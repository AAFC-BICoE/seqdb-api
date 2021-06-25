package ca.gc.aafc.seqdb.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.testsupport.factories.PcrPrimerFactory;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.QuerySpec;

public class SimpleFilterHandlerIT extends BaseRepositoryTest {
  
  @Test
  public void searchPrimers_whenNameFilterIsSet_filteredPrimersAreReturned() {
    
    String expectedPrimerName = "primer2";
    
    PcrPrimer primer1 = PcrPrimerFactory.newPcrPrimer().build(); //name is a random string each call to build();
    
    PcrPrimer primer2 = PcrPrimerFactory.newPcrPrimer().name(expectedPrimerName).build();// so we need to set a name to query
    
    PcrPrimer primer3 = PcrPrimerFactory.newPcrPrimer().build();
    
    PcrPrimer primer20 = PcrPrimerFactory.newPcrPrimer().build();
    
    for (PcrPrimer newPrimer : Arrays.asList(primer1, primer2, primer3, primer20)) {
      persist(newPrimer);
    }
    
    QuerySpec querySpec = new QuerySpec(PcrPrimerDto.class);
    querySpec.addFilter(new FilterSpec(Arrays.asList("name"), FilterOperator.EQ, expectedPrimerName));
    List<PcrPrimerDto> primerDtos = this.pcrPrimerRepository.findAll(querySpec);
    
    assertEquals(
        Arrays.asList(expectedPrimerName),
        primerDtos.stream().map(PcrPrimerDto::getName).collect(Collectors.toList())
    );
    
  }

}
