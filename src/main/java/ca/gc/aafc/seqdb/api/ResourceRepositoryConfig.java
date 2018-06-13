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
import ca.gc.aafc.seqdb.api.repository.JpaDtoRepository;
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

  /**
   * Configures DTO-to-Entity mappings.
   * @return the DtoJpaMapper
   */
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
  public JpaResourceRepository<PcrPrimerDto> pcrPrimerRepository(JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(PcrPrimerDto.class, selectionHandler, filterHandler,
        dtoRepository);
  }

  @Bean
  public JpaResourceRepository<RegionDto> regionRepository(JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(RegionDto.class, selectionHandler, filterHandler,
        dtoRepository);
  }

  @Bean
  public JpaResourceRepository<PcrBatchDto> pcrBatchRepository(JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(PcrBatchDto.class, selectionHandler, filterHandler,
        dtoRepository);
  }

  @Bean
  public JpaResourceRepository<PcrReactionDto> pcrReactionRepository(
      JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(PcrReactionDto.class, selectionHandler, filterHandler,
        dtoRepository);
  }

  @Bean
  public JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository(
      DtoJpaMapper dtoJpaMapper, JpaDtoRepository dtoRepository) {
    return new JpaRelationshipRepository<>(PcrPrimerDto.class, RegionDto.class, entityManager,
        dtoJpaMapper, dtoRepository, filterHandler);
  }

  @Bean
  public JpaRelationshipRepository<PcrBatchDto, PcrReactionDto> pcrBatchToPcrReactionRepository(
      DtoJpaMapper dtoJpaMapper, JpaDtoRepository dtoRepository) {
    return new JpaRelationshipRepository<>(PcrBatchDto.class, PcrReactionDto.class, entityManager,
        dtoJpaMapper, dtoRepository, filterHandler);
  }

  @Bean
  public JpaRelationshipRepository<PcrReactionDto, PcrBatchDto> pcrReactionToPcrBatchRepository(
      DtoJpaMapper dtoJpaMapper, JpaDtoRepository dtoRepository) {
    return new JpaRelationshipRepository<>(PcrReactionDto.class, PcrBatchDto.class, entityManager,
        dtoJpaMapper, dtoRepository, filterHandler);
  }

}
