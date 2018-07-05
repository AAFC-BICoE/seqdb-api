package ca.gc.aafc.seqdb.api;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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
import ca.gc.aafc.seqdb.api.repository.handlers.JpaDtoMapper;
import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
import ca.gc.aafc.seqdb.entities.PcrBatch;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.PcrReaction;
import ca.gc.aafc.seqdb.entities.Region;

@Configuration
@EntityScan("ca.gc.aafc.seqdb.entities")
public class ResourceRepositoryConfig {

  @Inject
  private FilterHandler filterHandler;

  /**
   * Configures DTO-to-Entity mappings.
   * 
   * @return the DtoJpaMapper
   */
  @Bean
  public JpaDtoMapper dtoJpaMapper() {
    Map<Class<?>, Class<?>> jpaEntities = new HashMap<>();

    jpaEntities.put(RegionDto.class, Region.class);
    jpaEntities.put(PcrPrimerDto.class, PcrPrimer.class);
    jpaEntities.put(PcrBatchDto.class, PcrBatch.class);
    jpaEntities.put(PcrReactionDto.class, PcrReaction.class);

    return new JpaDtoMapper(jpaEntities);
  }

  @Bean
  public JpaResourceRepository<PcrPrimerDto> pcrPrimerRepository(JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(PcrPrimerDto.class, dtoRepository, filterHandler);
  }

  @Bean
  public JpaResourceRepository<RegionDto> regionRepository(JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(RegionDto.class, dtoRepository, filterHandler);
  }

  @Bean
  public JpaResourceRepository<PcrBatchDto> pcrBatchRepository(JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(PcrBatchDto.class, dtoRepository, filterHandler);
  }

  @Bean
  public JpaResourceRepository<PcrReactionDto> pcrReactionRepository(
      JpaDtoRepository dtoRepository) {
    return new JpaResourceRepository<>(PcrReactionDto.class, dtoRepository, filterHandler);
  }

  @Bean
  public JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository) {
    return new JpaRelationshipRepository<>(PcrPrimerDto.class, RegionDto.class, dtoRepository,
        filterHandler);
  }

  @Bean
  public JpaRelationshipRepository<PcrBatchDto, PcrReactionDto> pcrBatchToPcrReactionRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository) {
    return new JpaRelationshipRepository<>(PcrBatchDto.class, PcrReactionDto.class, dtoRepository,
        filterHandler);
  }

  @Bean
  public JpaRelationshipRepository<PcrReactionDto, PcrBatchDto> pcrReactionToPcrBatchRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository) {
    return new JpaRelationshipRepository<>(PcrReactionDto.class, PcrBatchDto.class, dtoRepository,
        filterHandler);
  }

}
