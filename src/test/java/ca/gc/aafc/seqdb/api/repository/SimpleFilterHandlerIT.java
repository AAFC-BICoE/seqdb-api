package ca.gc.aafc.seqdb.api.repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Test;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PcrPrimerBuilder;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import ca.gc.aafc.seqdb.factories.PcrPrimerFactory;
import io.crnk.core.queryspec.FilterOperator;
import io.crnk.core.queryspec.FilterSpec;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;

public class SimpleFilterHandlerIT extends BaseRepositoryTest {
  
  private static final Integer TEST_PRIMER_LOT_NUMBER = 1;
  private static final PrimerType TEST_PRIMER_TYPE = PrimerType.PRIMER;
  private static final String TEST_PRIMER_SEQ = "test seq";
  
  private static PcrPrimerBuilder ITPcrPrimerFactory() {
    return PcrPrimerFactory.newPcrPrimer()
        .lotNumber(TEST_PRIMER_LOT_NUMBER)
        .type(TEST_PRIMER_TYPE)
        .seq(TEST_PRIMER_SEQ);
  }

  @Inject
  private ResourceRepositoryV2<PcrPrimerDto, Serializable> primerRepository;
  
  @Test
  public void searchPrimers_whenNameFilterIsSet_filteredPrimersAreReturned() {
    
    final String expectedPrimerName = "primer2";
    
    PcrPrimer primer1 = ITPcrPrimerFactory().build(); //name is a random string each call to build();
    
    PcrPrimer primer2 = ITPcrPrimerFactory().name(expectedPrimerName).build();
    
    PcrPrimer primer3 = ITPcrPrimerFactory().build();
    
    PcrPrimer primer20 = ITPcrPrimerFactory().build();
    
    for (PcrPrimer newPrimer : Arrays.asList(primer1, primer2, primer3, primer20)) {
      System.out.println(newPrimer.getName());
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
