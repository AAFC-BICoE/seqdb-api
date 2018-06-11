package ca.gc.aafc.seqdb.api;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.gc.aafc.seqdb.api.dto.PcrBatchDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.PcrReactionDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.repository.JpaRelationshipRepository;
import ca.gc.aafc.seqdb.api.repository.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.handlers.DtoJpaMapper;
import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
import ca.gc.aafc.seqdb.api.repository.handlers.SelectionHandler;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Region;

@Configuration
@EntityScan("ca.gc.aafc.seqdb.entities")
public class ResourceRepositoryConfig {

  @Inject
  private FilterHandler filterHandler;

  @Inject
  private SelectionHandler selectionHandler;

  @Inject
  private EntityManager entityManager;

  @Bean
  public DtoJpaMapper dtoJpaMapper() {
    Map<Class<?>, Class<?>> jpaEntities = new HashMap<>();

    jpaEntities.put(RegionDto.class, Region.class);
    jpaEntities.put(PcrPrimerDto.class, PcrPrimer.class);
    jpaEntities.put(PcrBatchDto.class, PcrBatch.class);
    jpaEntities.put(PcrReactionDto.class, PcrReaction.class);

    return new DtoJpaMapper(jpaEntities);
  }

  @Bean
  public JpaResourceRepository<PcrPrimerDto, PcrPrimer> pcrPrimerRepository() {
    return new JpaResourceRepository<>(PcrPrimerDto.class, PcrPrimer.class, entityManager,
        selectionHandler, filterHandler);
  }

  @Bean
  public JpaResourceRepository<RegionDto, Region> regionRepository() {
    return new JpaResourceRepository<>(RegionDto.class, Region.class, entityManager,
        selectionHandler, filterHandler);
  }
  
  @Bean
  public JpaResourceRepository<PcrBatchDto, PcrBatch> pcrBatchRepository() {
    return new JpaResourceRepository<>(PcrBatchDto.class, PcrBatch.class, entityManager,
        selectionHandler, filterHandler);
  }
  
  @Bean
  public JpaResourceRepository<PcrReactionDto, PcrReaction> pcrReactionRepository() {
    return new JpaResourceRepository<>(PcrReactionDto.class, PcrReaction.class, entityManager,
        selectionHandler, filterHandler);
  }

  @Bean
  public JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository(DtoJpaMapper dtoJpaMapper) {
    return new JpaRelationshipRepository<>(PcrPrimerDto.class, RegionDto.class, entityManager,
        dtoJpaMapper, selectionHandler, filterHandler);
  }
  
  @Bean
  public JpaRelationshipRepository<PcrBatchDto, PcrReactionDto> pcrBatchToPcrReactionRepository(DtoJpaMapper dtoJpaMapper) {
    return new JpaRelationshipRepository<>(PcrBatchDto.class, PcrReactionDto.class, entityManager,
        dtoJpaMapper, selectionHandler, filterHandler);
  }
  
  @Bean
  public JpaRelationshipRepository<PcrReactionDto, PcrBatchDto> pcrReactionToPcrBatchRepository(DtoJpaMapper dtoJpaMapper) {
    return new JpaRelationshipRepository<>(PcrReactionDto.class, PcrBatchDto.class, entityManager,
        dtoJpaMapper, selectionHandler, filterHandler);
  }

}
