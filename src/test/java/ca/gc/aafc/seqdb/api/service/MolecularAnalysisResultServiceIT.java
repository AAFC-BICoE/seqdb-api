package ca.gc.aafc.seqdb.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import ca.gc.aafc.dina.messaging.DinaEventPublisher;
import ca.gc.aafc.dina.messaging.EntityChanged;
import ca.gc.aafc.seqdb.api.SequenceModuleBaseIT;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisResult;
import ca.gc.aafc.seqdb.api.entities.MolecularAnalysisRun;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisResultFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisRunFactory;
import ca.gc.aafc.seqdb.api.testsupport.factories.MolecularAnalysisRunItemFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import(MolecularAnalysisResultServiceIT.RecordingPublisherConfiguration.class)
public class MolecularAnalysisResultServiceIT extends SequenceModuleBaseIT {

  // more than the default page size (100) to make sure no run is dropped
  private static final int NUMBER_OF_RUNS = 105;

  @Autowired
  private RecordingEventPublisher eventPublisher;

  @TestConfiguration
  public static class RecordingPublisherConfiguration {
    @Bean
    @Primary
    public RecordingEventPublisher recordingEventPublisher() {
      return new RecordingEventPublisher();
    }
  }

  /**
   * Keeps every published event in memory so tests can inspect them.
   */
  public static class RecordingEventPublisher implements DinaEventPublisher<EntityChanged> {
    private final List<EntityChanged> events = new CopyOnWriteArrayList<>();

    @Override
    public void addEvent(EntityChanged event) {
      events.add(event);
    }

    public List<EntityChanged> getEvents() {
      return new ArrayList<>(events);
    }

    public void clear() {
      events.clear();
    }
  }

  @BeforeEach
  public void clearEvents() {
    eventPublisher.clear();
  }

  @Test
  public void onUpdateResult_eventPublishedOncePerLinkedRun() {
    MolecularAnalysisResult result = MolecularAnalysisResultFactory
      .newMolecularAnalysisResult().build();
    molecularAnalysisResultService.create(result);

    Set<UUID> expectedRunUuids = new HashSet<>();
    for (int i = 0; i < NUMBER_OF_RUNS; i++) {
      MolecularAnalysisRun run = MolecularAnalysisRunFactory.newMolecularAnalysisRun().build();
      molecularAnalysisRunService.create(run);
      expectedRunUuids.add(run.getUuid());

      // 2 items on the same run must produce a single event
      for (int j = 0; j < 2; j++) {
        molecularAnalysisRunItemService.create(
          MolecularAnalysisRunItemFactory.newMolecularAnalysisRunItem(run).result(result).build());
      }
    }

    eventPublisher.clear();
    molecularAnalysisResultService.update(result);

    List<EntityChanged> events = eventPublisher.getEvents();
    assertEquals(NUMBER_OF_RUNS, events.size());
    assertEquals(expectedRunUuids,
      events.stream().map(EntityChanged::getUuid).collect(Collectors.toSet()));
  }
}
