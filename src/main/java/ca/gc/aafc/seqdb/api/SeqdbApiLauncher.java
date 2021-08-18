package ca.gc.aafc.seqdb.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ca.gc.aafc.dina.service.JaversDataService;

/**
 * Launches the application.
 * @author poffm
 */
//CHECKSTYLE:OFF HideUtilityClassConstructor (Configuration class can not have invisible constructor, ignore the check style error for this case)
@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
@MapperScan(basePackageClasses = JaversDataService.class)
public class SeqdbApiLauncher {

  public static void main(String[] args) {
    SpringApplication.run(SeqdbApiLauncher.class, args);
  }

}
