package ca.gc.aafc.seqdb.api.auditing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.seqdb.api.entities.PcrPrimer;
import ca.gc.aafc.seqdb.api.entities.Region;
import ca.gc.aafc.seqdb.api.testsupport.factories.PcrPrimerFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.RegionFactory;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class AuditListenerIT {

  @Inject
  private Javers javers;

  @Inject
  private BaseDAO baseDao;

  @Inject
  private EntityManager entityManager;

  @Test
  public void adda_whenAdded_snapshotCreated() {
    PcrPrimer pcrPrimer = addPcrPrimer();
    CdoSnapshot latest = javers.getLatestSnapshot(pcrPrimer.getPcrPrimerId().toString(), PcrPrimer.class).get();
    assertEquals("INITIAL", latest.getType().toString()); // INITIAL snapshot created.
    assertEquals(1, snapshotCount(pcrPrimer));
  }
  
  @Test
  public void updatePcrPrimer_whenUpdated_snapshotCreated() {
    PcrPrimer pcrPrimer = addPcrPrimer();
    pcrPrimer.setDesignedBy("BICOE");
    entityManager.flush();
    CdoSnapshot latest = javers.getLatestSnapshot(pcrPrimer.getPcrPrimerId().toString(), PcrPrimer.class).get();
    assertEquals("UPDATE", latest.getType().toString()); // UPDATE snapshot created.
    assertEquals(Arrays.asList("designedBy"), latest.getChanged()); // UPDATE snapshot created.
    assertEquals(2, snapshotCount(pcrPrimer));
  }
    
  @Test
  public void deletePcrPrimer_whenDeleted_snapshotCreated() {
    PcrPrimer pcrPrimer = addPcrPrimer();
    baseDao.delete(pcrPrimer);
    entityManager.flush();
    CdoSnapshot latest = javers.getLatestSnapshot(pcrPrimer.getPcrPrimerId().toString(), PcrPrimer.class).get();
    assertEquals("TERMINAL", latest.getType().toString()); // TERMINAL snapshot created.
    assertEquals(2, snapshotCount(pcrPrimer));
  }

  private PcrPrimer addPcrPrimer() {
    Region testRegion = RegionFactory.newRegion().build();
    entityManager.persist(testRegion);
    entityManager.flush();
    
    PcrPrimer pcrPrimer = PcrPrimerFactory.newPcrPrimer()
        .region(testRegion).build();
    entityManager.persist(pcrPrimer);
    entityManager.flush();
    return pcrPrimer;
  }

  private int snapshotCount(PcrPrimer pcrPrimer) {
    JqlQuery query = QueryBuilder.byInstanceId(pcrPrimer.getPcrPrimerId().toString(), "pcrPrimer").build();
    List<CdoSnapshot> snapshots = javers.findSnapshots(query);
    return snapshots.size();
  }

}
