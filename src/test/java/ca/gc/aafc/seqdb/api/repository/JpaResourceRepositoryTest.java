package ca.gc.aafc.seqdb.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrPrimer.PrimerType;
import io.crnk.core.engine.registry.ResourceRegistry;
import io.crnk.core.exception.ResourceNotFoundException;
import io.crnk.core.queryspec.IncludeFieldSpec;
import io.crnk.core.queryspec.QuerySpec;

public class JpaResourceRepositoryTest extends BaseIntegrationTest {

  private static final String TEST_PRIMER_NAME = "test primer";
  private static final Integer TEST_PRIMER_LOT_NUMBER = 1;
  private static final PrimerType TEST_PRIMER_TYPE = PrimerType.PRIMER;
  private static final String TEST_PRIMER_SEQ = "test seq";
  
  @Inject
  private JpaResourceRepository<PcrPrimerDto, PcrPrimer> primerRepository;
  
  @Inject
  private ResourceRegistry resourceRegistry;
  
  /**
   * Crnk injects the resource repository into "ResourceRegistryAware"-implementing repositories on
   * each request before executing the repository's CRUD method. The ResourceRegistry does not
   * provide correct resource metadata when injected into the repository by constructor. This method
   * simulates crnk's behavior of injecting the ResourceRegistry on each request.
   */
  @Before
  public void initRepository() {
    primerRepository.setResourceRegistry(resourceRegistry);
  }

  /**
   * Persists a PcrPrimer with the required fields set.
   *
   * @return the persisted primer
   */
  private PcrPrimer persistTestPrimer() {
    PcrPrimer primer = new PcrPrimer();
    primer.setName(TEST_PRIMER_NAME);
    primer.setLotNumber(TEST_PRIMER_LOT_NUMBER);
    primer.setType(TEST_PRIMER_TYPE);
    primer.setSeq(TEST_PRIMER_SEQ);

    assertNull(primer.getId());
    entityManager.persist(primer);
    // New primer must have an ID.
    assertNotNull(primer.getId());

    return primer;
  }

  private static List<IncludeFieldSpec> includeFieldSpecs(String... includedFields) {
    return Arrays.asList(includedFields)
        .stream()
        .map(Arrays::asList)
        .map(IncludeFieldSpec::new)
        .collect(Collectors.toList());
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
