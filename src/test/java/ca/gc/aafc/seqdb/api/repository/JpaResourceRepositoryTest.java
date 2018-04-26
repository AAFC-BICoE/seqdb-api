package ca.gc.aafc.seqdb.api.repository;

import javax.inject.Inject;

import org.junit.Test;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.QuerySpec;

public class JpaResourceRepositoryTest extends BaseIntegrationTest {

  @Inject
  private JpaResourceRepository<PcrPrimerDto, PcrPrimer> primerRepository;

  private static final String TEST_PRIMER_NAME = "test primer";
  private static final PrimerType TEST_PRIMER_TYPE = PrimerType.PRIMER;
  private static final String TEST_PRIMER_SEQ = "test seq";
  private static final Integer TEST_PRIMER_LOT_NUMBER = 1;
  
  /**
   * Persists a PcrPrimer with the required fields set.
   * 
   * @return the persisted primer
   */
  private PcrPrimer persistTestPrimer() {
    PcrPrimer primer = new PcrPrimer();
    primer.setName(TEST_PRIMER_NAME);
    primer.setType(TEST_PRIMER_TYPE);
    primer.setSeq(TEST_PRIMER_SEQ);
    primer.setLotNumber(TEST_PRIMER_LOT_NUMBER);
    
    assertNull(primer.getId());
    entityManager.persist(primer);
    assertNotNull(primer.getId());
    
    return primer;
  }
  
  @Test
  public void findOnePrimer_onRepositoryReturn_primerFound() {
    PcrPrimer primer = persistTestPrimer();
    
    // New primer must have an ID.
    Integer newPrimerId = primer.getId();
    assertNotNull(newPrimerId);

    PcrPrimerDto primerDto = primerRepository.findOne(
      newPrimerId,
      new QuerySpec(PcrPrimerDto.class)
    );

    // Returned primer DTO must have correct values.
    assertNotNull(primerDto);
    assertEquals(newPrimerId, primerDto.getId());
    assertEquals(TEST_PRIMER_NAME, primerDto.getName());
    assertEquals(TEST_PRIMER_TYPE.getValue(), primerDto.getType().getValue());
    assertEquals(TEST_PRIMER_SEQ, primerDto.getSeq());
    assertEquals(TEST_PRIMER_LOT_NUMBER, primerDto.getLotNumber());

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
