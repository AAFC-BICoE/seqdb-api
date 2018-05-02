package ca.gc.aafc.seqdb.api;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.repository.JpaRelationshipRepository;
import ca.gc.aafc.seqdb.api.repository.JpaResourceRepository;
import ca.gc.aafc.seqdb.api.repository.handlers.FieldsHandler;
import ca.gc.aafc.seqdb.api.repository.handlers.IncludeHandler;
import ca.gc.aafc.seqdb.entities.PcrPrimer;
import ca.gc.aafc.seqdb.entities.Region;

@Configuration
public class ResourceRepositoryConfig {

  @Bean
  public JpaResourceRepository<PcrPrimerDto, PcrPrimer> pcrPrimerRepository(EntityManager entityManager, FieldsHandler fieldsHandler, IncludeHandler includeHandler) {
    return new JpaResourceRepository<>(PcrPrimerDto.class, PcrPrimer.class, entityManager, fieldsHandler, includeHandler);
  }
  
  @Bean
  public JpaResourceRepository<RegionDto, Region> regionRepository(EntityManager entityManager, FieldsHandler fieldsHandler, IncludeHandler includeHandler) {
    return new JpaResourceRepository<>(RegionDto.class, Region.class, entityManager, fieldsHandler, includeHandler);
  }
  
  @Bean
  public JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository() {
    return new JpaRelationshipRepository<>(PcrPrimerDto.class, RegionDto.class);
  }
  
  @Bean
  public FieldsHandler fieldsHandler(EntityManager entityManager) {
    return new FieldsHandler(entityManager);
  }
  
  @Bean
  public IncludeHandler inlcludeHandler(EntityManager entityManager) {
    return new IncludeHandler();
  }
  
}
