package ca.gc.aafc.seqdb.api;

import javax.persistence.EntityManager;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.repository.JpaRelationshipRepository;
import ca.gc.aafc.seqdb.api.repository.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.handlers.FilterHandler;
import ca.gc.aafc.seqdb.api.repository.handlers.SelectionHandler;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.Region;

@Configuration
@EntityScan("ca.gc.aafc.seqdb.entities")
public class ResourceRepositoryConfig {

  @Bean
  public JpaResourceRepository<PcrPrimerDto, PcrPrimer> pcrPrimerRepository(
      EntityManager entityManager, SelectionHandler selectionHandler, FilterHandler filterHandler) {
    return new JpaResourceRepository<>(PcrPrimerDto.class, PcrPrimer.class, entityManager,
        selectionHandler, filterHandler);
  }

  @Bean
  public JpaResourceRepository<RegionDto, Region> regionRepository(EntityManager entityManager,
      SelectionHandler selectionHandler, FilterHandler filterHandler) {
    return new JpaResourceRepository<>(RegionDto.class, Region.class, entityManager,
        selectionHandler, filterHandler);
  }

  @Bean
  public JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository(
      JpaResourceRepository<PcrPrimerDto, PcrPrimer> pcrPrimerRepository,
      JpaResourceRepository<RegionDto, Region> regionRepository) {
    return new JpaRelationshipRepository<>(PcrPrimerDto.class, RegionDto.class);
  }

}
