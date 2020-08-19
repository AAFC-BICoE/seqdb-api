package ca.gc.aafc.seqdb.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ca.gc.aafc.dina.DinaBaseApiAutoConfiguration;
import ca.gc.aafc.dina.dto.RelatedEntity;
import ca.gc.aafc.dina.filter.RsqlFilterHandler;
import ca.gc.aafc.dina.filter.SimpleFilterHandler;
import ca.gc.aafc.dina.jpa.BaseDAO;
import ca.gc.aafc.dina.mapper.CustomFieldResolverSpec;
import ca.gc.aafc.dina.mapper.JpaDtoMapper;
import ca.gc.aafc.dina.repository.JpaDtoRepository;
import ca.gc.aafc.dina.repository.JpaRelationshipRepository;
import ca.gc.aafc.dina.repository.meta.JpaTotalMetaInformationProvider;
import ca.gc.aafc.dina.util.ClassAnnotationHelper;
import ca.gc.aafc.seqdb.api.dto.ChainDto;
import ca.gc.aafc.seqdb.api.dto.ChainStepTemplateDto;
import ca.gc.aafc.seqdb.api.dto.ChainTemplateDto;
import ca.gc.aafc.seqdb.api.dto.IndexSetDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolContentDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPoolDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepBatchDto;
import ca.gc.aafc.seqdb.api.dto.LibraryPrepDto;
import ca.gc.aafc.seqdb.api.dto.NgsIndexDto;
import ca.gc.aafc.seqdb.api.dto.PcrPrimerDto;
import ca.gc.aafc.seqdb.api.dto.PreLibraryPrepDto;
import ca.gc.aafc.seqdb.api.dto.ProductDto;
import ca.gc.aafc.seqdb.api.dto.ProtocolDto;
import ca.gc.aafc.seqdb.api.dto.RegionDto;
import ca.gc.aafc.seqdb.api.dto.SampleDto;
import ca.gc.aafc.seqdb.api.dto.StepResourceDto;
import ca.gc.aafc.seqdb.api.dto.StepTemplateDto;
import ca.gc.aafc.seqdb.api.repository.VocabularyReadOnlyRepository;

@Configuration
//Restricted to repository package so it won't affect tests with bean mocking/overriding.
@ComponentScan("ca.gc.aafc.seqdb.api.repository")
@EntityScan("ca.gc.aafc.seqdb.api.entities")
@ComponentScan(basePackageClasses = DinaBaseApiAutoConfiguration.class)
public class ResourceRepositoryConfig {

  @Inject
  private SimpleFilterHandler simpleFilterHandler;
  
  @Inject
  private RsqlFilterHandler rsqlFilterHandler;
  
  /**
   * Configures DTO-to-Entity mappings.
   * 
   * @return the DtoJpaMapper
   */
  @Bean
  public JpaDtoMapper dtoJpaMapper(BaseDAO baseDAO) {
    Map<Class<?>, List<CustomFieldResolverSpec<?>>> customFieldResolvers = new HashMap<>();

    // Map all DTOs to their related Entities.
    Map<Class<?>, Class<?>> entitiesMap = ClassAnnotationHelper
      .findAnnotatedClasses(RegionDto.class, RelatedEntity.class)
      .stream()
      .collect(
        Collectors.toMap(
          Function.identity(),
          clazz -> clazz.getAnnotation(RelatedEntity.class).value()));
    
    return new JpaDtoMapper(entitiesMap, customFieldResolvers);
  }
  
  @Bean
  public VocabularyReadOnlyRepository vocabularyDto(){
    return new VocabularyReadOnlyRepository();
  }
  
  @Bean
  public JpaRelationshipRepository<PcrPrimerDto, RegionDto> primerToRegionRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        PcrPrimerDto.class,
        RegionDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler,
            rsqlFilterHandler
        ),
        metaInformationProvider
    );
  }

  @Bean
  public JpaRelationshipRepository<ProtocolDto, ProductDto> protocolToProductRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        ProtocolDto.class,
        ProductDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler, 
            rsqlFilterHandler
            ),
        metaInformationProvider
    );
  }  

  /**
   * Relationship Repository between a Chain and ChainTemplate.
   */
  @Bean
  public JpaRelationshipRepository<ChainDto, ChainTemplateDto> chainToChainTemplateRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        ChainDto.class,
        ChainTemplateDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  /**
   * Relationship Repository between a ChainStepTemplate and ChainTemplate.
   */
  @Bean
  public JpaRelationshipRepository<ChainStepTemplateDto, ChainTemplateDto> chainStepTemplateToChainTemplateRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        ChainStepTemplateDto.class,
        ChainTemplateDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  /**
   * Relationship Repository between a ChainStepTemplate and StepTemplate.
   */
  @Bean
  public JpaRelationshipRepository<ChainStepTemplateDto, StepTemplateDto> chainStepTemplateToStepTemplateRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        ChainStepTemplateDto.class,
        StepTemplateDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  /**
   * Relationship Repository between a StepResource and Chain.
   */
  @Bean
  public JpaRelationshipRepository<StepResourceDto, ChainDto> stepResourceToChainRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        StepResourceDto.class,
        ChainDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  /**
   * Relationship Repository between a StepResource and Region.
   */
  @Bean
  public JpaRelationshipRepository<StepResourceDto, RegionDto> stepResourceToRegionRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        StepResourceDto.class,
        RegionDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  /**
   * Relationship Repository between a StepResource and PcrPrimer.
   */
  @Bean
  public JpaRelationshipRepository<StepResourceDto, PcrPrimerDto> stepResourceToPcrPrimerRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        StepResourceDto.class,
        PcrPrimerDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  /**
   * Relationship Repository between a StepResource and Sample.
   */
  @Bean
  public JpaRelationshipRepository<StepResourceDto, SampleDto> stepResourceToSampleRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        StepResourceDto.class,
        SampleDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  @Bean
  public JpaRelationshipRepository<SampleDto, ProductDto> sampleToProductRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        SampleDto.class, 
        ProductDto.class, 
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler, 
            rsqlFilterHandler),
        metaInformationProvider);
  }
  
  @Bean
  public JpaRelationshipRepository<SampleDto, ProtocolDto> sampleToProtocolRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        SampleDto.class, 
        ProtocolDto.class, 
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler, 
            rsqlFilterHandler),
        metaInformationProvider);
  }
  
  /**
   * Relationship Repository between a StepResource and ChainStepTemplate.
   */
  @Bean
  public JpaRelationshipRepository<StepResourceDto, ChainStepTemplateDto> stepResourceToChainStepTemplateRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        StepResourceDto.class,
        ChainStepTemplateDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  @Bean
  public JpaRelationshipRepository<StepResourceDto, PreLibraryPrepDto> stepResourceToPreLibraryPrepRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        StepResourceDto.class,
        PreLibraryPrepDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  @Bean
  public JpaRelationshipRepository<PreLibraryPrepDto, ProtocolDto> preLibraryPrepToProtocolRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        PreLibraryPrepDto.class,
        ProtocolDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  @Bean
  public JpaRelationshipRepository<PreLibraryPrepDto, ProductDto> preLibraryPrepToProductRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        PreLibraryPrepDto.class,
        ProductDto.class,
        dtoRepository,
        Arrays.asList(
            simpleFilterHandler
        ),
        metaInformationProvider
    );
  }
  
  @Bean
  public JpaRelationshipRepository<LibraryPrepBatchDto, LibraryPrepDto> libraryPrepBatchToLibraryPrepRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(
        LibraryPrepBatchDto.class,
        LibraryPrepDto.class,
        dtoRepository,
        Arrays.asList(
            rsqlFilterHandler
        ),
        metaInformationProvider
    );
  }

  @Bean
  public JpaRelationshipRepository<IndexSetDto, NgsIndexDto> indexSetToNgsIndexesRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(IndexSetDto.class, NgsIndexDto.class, dtoRepository,
        Arrays.asList(rsqlFilterHandler), metaInformationProvider);
  }

  @Bean
  public JpaRelationshipRepository<LibraryPoolDto, LibraryPoolContentDto> libraryPoolToContentRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(LibraryPoolDto.class, LibraryPoolContentDto.class,
        dtoRepository, Arrays.asList(rsqlFilterHandler), metaInformationProvider);
  }

  @Bean
  public JpaRelationshipRepository<LibraryPoolContentDto, LibraryPoolDto> libraryPoolContentToPoolRepository(
      JpaDtoMapper dtoJpaMapper, JpaDtoRepository dtoRepository, JpaTotalMetaInformationProvider metaInformationProvider) {
    return new JpaRelationshipRepository<>(LibraryPoolContentDto.class, LibraryPoolDto.class,
        dtoRepository, Arrays.asList(rsqlFilterHandler), metaInformationProvider);
  }
}
