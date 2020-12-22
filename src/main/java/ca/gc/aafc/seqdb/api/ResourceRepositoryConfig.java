package ca.gc.aafc.seqdb.api;

import ca.gc.aafc.dina.DinaBaseApiAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//Restricted to repository package so it won't affect tests with bean mocking/overriding.
@ComponentScan("ca.gc.aafc.seqdb.api.repository")
@EntityScan("ca.gc.aafc.seqdb.api.entities")
@ComponentScan(basePackageClasses = DinaBaseApiAutoConfiguration.class)
public class ResourceRepositoryConfig {
}
