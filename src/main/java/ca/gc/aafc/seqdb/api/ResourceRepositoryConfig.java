package ca.gc.aafc.seqdb.api;

import io.crnk.core.engine.registry.ResourceRegistry;
import javax.inject.Inject;

import ca.gc.aafc.dina.DinaBaseApiAutoConfiguration;
import ca.gc.aafc.seqdb.api.dto.SequenceManagedAttributeDto;
import ca.gc.aafc.seqdb.api.util.ManagedAttributeIdMapper;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//Restricted to repository package so it won't affect tests with bean mocking/overriding.
@ComponentScan("ca.gc.aafc.seqdb.api.repository")
@EntityScan("ca.gc.aafc.seqdb.api.entities")
@ComponentScan(basePackageClasses = DinaBaseApiAutoConfiguration.class)
public class ResourceRepositoryConfig {

  @Inject
  @SuppressWarnings({"deprecation", "unchecked"})
  public void setupManagedAttributeLookup(ResourceRegistry resourceRegistry) {
    var resourceInfo = resourceRegistry.getEntry(SequenceManagedAttributeDto.class)
      .getResourceInformation();

    resourceInfo.setIdStringMapper(
      new ManagedAttributeIdMapper(resourceInfo.getIdStringMapper()));
  }
}
