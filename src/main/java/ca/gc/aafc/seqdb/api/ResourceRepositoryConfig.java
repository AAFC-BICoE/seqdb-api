package ca.gc.aafc.seqdb.api;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.repository.JpaResourceRepository;
import ca.gc.aafc.seqdb.entities.PcrPrimer;

@Configuration
public class ResourceRepositoryConfig {

  @Bean
  public JpaResourceRepository<PcrPrimerDto, PcrPrimer> pcrPrimerRepository(EntityManager entityManager) {
    return new JpaResourceRepository<>(PcrPrimerDto.class, PcrPrimer.class, entityManager);
  }

}
